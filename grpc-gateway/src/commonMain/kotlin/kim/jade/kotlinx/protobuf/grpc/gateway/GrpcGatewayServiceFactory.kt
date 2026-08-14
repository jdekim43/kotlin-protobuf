package kim.jade.kotlinx.protobuf.grpc.gateway

import kim.jade.kotlinx.protobuf.type.ProtobufService
import kim.jade.kotlinx.protobuf.type.ProtobufServiceFactory

interface GrpcGatewayServiceFactory<I : ProtobufService, C : I> : ProtobufServiceFactory<I, C, GrpcGatewayClientOption>
