package com.example.teamA.processor

import com.example.teamA.entity.ResponseMessage
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.observables.ConnectableObservable
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.security.SecureRandom
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


@Component
class ResponsePublisher {
    var publisher : Flowable<ResponseMessage>
    private val logger
        = LoggerFactory.getLogger(ResponsePublisher::class.java)
    private val rand: Random = SecureRandom()

    constructor(){
        val responseObservable : Observable<ResponseMessage>
            = Observable.create {
                emitter : ObservableEmitter<ResponseMessage> ->
                val executorService = Executors.newScheduledThreadPool(1)
                executorService.scheduleAtFixedRate(newResponse(emitter), 0, 2, TimeUnit.SECONDS)
            }

        val connectableObservable : ConnectableObservable<ResponseMessage>
            = responseObservable.share().publish()
        connectableObservable.connect()
        publisher = connectableObservable.toFlowable(BackpressureStrategy.BUFFER)

     }

    private fun newResponse(emitter: ObservableEmitter<ResponseMessage>) : () -> Unit {
        return {
            val responseUpdates: MutableList<ResponseMessage> = getUpdates(rollDice(0, 5))
            emitResponse(emitter, responseUpdates)
        }
    }
    private fun rollDice(min: Int, max: Int): Int {
        return rand.nextInt(max - min + 1) + min
    }

    private fun getUpdates(number: Int): MutableList<ResponseMessage> {
        val updates: MutableList<ResponseMessage> = ArrayList<ResponseMessage>()
        for (i in 0 until number) {
            updates.add(rollUpdate())
        }
        return updates
    }

    private fun rollUpdate() : ResponseMessage{
        return ResponseMessage( "value : " + rollDice(0,100).toString() + ", at : " + Date().toString() )
    }

    private fun emitResponse(emitter: ObservableEmitter<ResponseMessage>, responseMessages : List<ResponseMessage>){
        responseMessages.forEach { responseMessage ->
            try {
                emitter.onNext(responseMessage)
            } catch (e: RuntimeException) {
                logger.error("Cannot send StockUpdate, e : {}", e)
            }
        }
    }


}