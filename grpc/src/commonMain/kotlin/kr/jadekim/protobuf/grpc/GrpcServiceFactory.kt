package kr.jadekim.protobuf.grpc

import kr.jadekim.protobuf.type.ProtobufService
import kr.jadekim.protobuf.type.ProtobufServiceFactory

interface GrpcServiceFactory<I : ProtobufService, C : I> : ProtobufServiceFactory<I, C, GrpcClientOption>
