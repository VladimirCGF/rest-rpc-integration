package br.com.ecommerce.orders;

import br.com.ecommerce.orders.dto.OrderRequest;
import br.com.ecommerce.payments.PaymentRequest;
import br.com.ecommerce.payments.PaymentResponse;
import br.com.ecommerce.payments.PaymentServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/orders")
public class OrderResource {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createOrder(OrderRequest order) {

        //Conectar ao servidor gRPC
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        PaymentServiceGrpc.PaymentServiceBlockingStub stub =
                PaymentServiceGrpc.newBlockingStub(channel);

        //Enviar dados para gRPC
        PaymentRequest paymentRequest = PaymentRequest.newBuilder()
                .setAmount(order.amount)
                .build();

        PaymentResponse grpcResponse = stub.processPayment(paymentRequest);

        channel.shutdown();

        return Response.ok(
                "{ \"paymentMessage\": \"" + grpcResponse.getMessage() + "\" }"
        ).build();
    }
}
