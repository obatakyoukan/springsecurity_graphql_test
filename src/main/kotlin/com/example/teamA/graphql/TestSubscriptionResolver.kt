package com.example.teamA.graphql

import com.example.teamA.entity.ResponseMessage
import com.example.teamA.processor.ResponsePublisher
import graphql.kickstart.tools.GraphQLSubscriptionResolver
import org.reactivestreams.Publisher
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.Date


@Component
class TestSubscriptionResolver : GraphQLSubscriptionResolver {

    @Autowired
    private lateinit var responsePublisher: ResponsePublisher

    fun timetest() : Publisher<ResponseMessage> {
        return responsePublisher.publisher
    }
}

