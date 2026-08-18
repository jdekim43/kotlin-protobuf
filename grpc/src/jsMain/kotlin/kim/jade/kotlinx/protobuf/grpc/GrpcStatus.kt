package kim.jade.kotlinx.protobuf.grpc

enum class GrpcStatus(val code: Int) {

    OK(0),
    CANCELLED(1),
    UNKNOWN(2),
    INVALID_ARGUMENT(3),
    DEADLINE_EXCEEDED(4),
    NOT_FOUND(5),
    ALREADY_EXISTS(6),
    PERMISSION_DENIED(7),
    RESOURCE_EXHAUSTED(8),
    FAILED_PRECONDITION(9),
    ABORTED(10),
    OUT_OF_RANGE(11),
    UNIMPLEMENTED(12),
    INTERNAL(13),
    UNAVAILABLE(14),
    DATA_LOSS(15),
    UNAUTHENTICATED(16);

    companion object {

        private val byCode: Map<Int, GrpcStatus> = entries.associateBy(GrpcStatus::code)

        fun forCode(code: Int?): GrpcStatus = byCode[code] ?: UNKNOWN
    }
}
