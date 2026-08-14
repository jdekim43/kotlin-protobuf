package kim.jade.kotlinx.protobuf.grpc

import kim.jade.kotlinx.protobuf.type.ProtobufService
import kim.jade.kotlinx.protobuf.type.ProtobufServiceFactory

interface GrpcServiceFactory<I : ProtobufService, C : I> : ProtobufServiceFactory<I, C, GrpcClientOption>
