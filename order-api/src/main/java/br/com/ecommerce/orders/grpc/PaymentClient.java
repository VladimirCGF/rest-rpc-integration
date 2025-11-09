package br.com.ecommerce.orders.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import br.com.ecommerce.payments.PaymentServiceGrpc;
import br.com.ecommerce.payments.PaymentRequest;
import br.com.ecommerce.payments.PaymentResponse;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PaymentClient {

    public PaymentResponse processPayment(double amount) {

        // conexão com o servidor gRPC Python rodando em localhost:50051
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 50051)
                .usePlaintext() // desabilita SSL/TLS
                .build();

        PaymentServiceGrpc.PaymentServiceBlockingStub stub =
                PaymentServiceGrpc.newBlockingStub(channel);

        PaymentResponse response = stub.processPayment(
                PaymentRequest.newBuilder()
                        .setAmount(amount)
                        .build()
        );

        channel.shutdown();
        return response;
    }
}
