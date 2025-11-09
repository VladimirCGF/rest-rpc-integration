import grpc
from concurrent import futures
import time

import payment_pb2
import payment_pb2_grpc

class PaymentService(payment_pb2_grpc.PaymentServiceServicer):
    def ProcessPayment(self, request, context):
        amount = request.amount
        if amount <= 0:
            return payment_pb2.PaymentResponse(approved=False, message="Valor inválido.")
        if amount < 1000:
            return payment_pb2.PaymentResponse(approved=True, message="Pagamento aprovado!")
        return payment_pb2.PaymentResponse(approved=False, message="Pagamento rejeitado.")

def serve():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    payment_pb2_grpc.add_PaymentServiceServicer_to_server(PaymentService(), server)
    server.add_insecure_port("[::]:50051")
    server.start()
    print("✅ Servidor gRPC rodando na porta 50051")
    server.wait_for_termination()

if __name__ == "__main__":
    serve()
