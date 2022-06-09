import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Struct;
import com.google.protobuf.Value;
import io.envoyproxy.envoy.config.core.v3.Locality;
import io.envoyproxy.envoy.config.core.v3.Node;
import io.envoyproxy.envoy.config.endpoint.v3.ClusterLoadAssignment;
import io.envoyproxy.envoy.config.listener.v3.Listener;
import io.envoyproxy.envoy.config.route.v3.RouteConfiguration;
import io.envoyproxy.envoy.extensions.filters.network.http_connection_manager.v3.HttpConnectionManager;
import io.envoyproxy.envoy.service.discovery.v3.DiscoveryRequest;
import io.envoyproxy.envoy.service.discovery.v3.DiscoveryResponse;
import io.grpc.stub.StreamObserver;

import java.util.HashMap;
import java.util.Map;

public class AdsResponseStreamObserver implements StreamObserver<DiscoveryResponse> {
    StreamObserver<io.envoyproxy.envoy.service.discovery.v3.DiscoveryRequest> requestStreamObserver;
    int indicator = 0;
    public AdsResponseStreamObserver() {
    }

    @Override
    public void onNext(DiscoveryResponse value) {

        try {
            System.out.println(value);
            System.out.println(indicator);
            if (indicator == 0) {
//                if (value.getResources(0).getTypeUrl().equals("Listener")) {
//
//                }

                Listener listener = Listener.parseFrom(value.getResources(0).getValue());
                HttpConnectionManager connectionManager = HttpConnectionManager.parseFrom(listener.getApiListener().getApiListener().getValue());
                System.out.println("Route Config");
                System.out.println(connectionManager.getRds().getRouteConfigName());
                sendRdsRequest();


            } else if (indicator == 1) {
                RouteConfiguration routeConfiguration = RouteConfiguration.parseFrom(value.getResources(0).getValue());
                sendCdsRequest();
            } else if (indicator == 2) {
                sendCLoadRequest();
            } else {
                ClusterLoadAssignment clusterLoadAssignment = ClusterLoadAssignment.parseFrom(value.getResources(0).getValue());
                System.out.println(clusterLoadAssignment);
            }



        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(Throwable t) {

        System.out.println("Err");
        System.out.println(t);

    }

    @Override
    public void onCompleted() {

        System.out.println("Done");

    }

    public void start(StreamObserver<io.envoyproxy.envoy.service.discovery.v3.DiscoveryRequest> requestStreamObserver) {
        this.requestStreamObserver = requestStreamObserver;
        sendLdsRequest();

    }

    public void sendLdsRequest() {
        if (requestStreamObserver != null) {
            Node node = Node.newBuilder()
                    .setId("b7f9c818-fb46-43ca-8662-d3bdbcf7ec18~10.0.0.1")
                    .setMetadata(Struct.newBuilder().putFields("R_GCP_PROJECT_NUMBER", Value.newBuilder().setStringValue("123456789012").build()).build())
                    .setLocality(Locality.newBuilder().setZone("v").build())
                    .setUserAgentVersion("1.41.0")
                    .setUserAgentName("gRPC Java")
                    .addClientFeatures("envoy.lb.does_not_support_overprovisioning")
                    .build();
            Map<String, String> ldsResourceSubscribers = new HashMap<>();
            ldsResourceSubscribers.put("be-srv", "be-srv");
            requestStreamObserver.onNext(
                    DiscoveryRequest.newBuilder()
                            .setVersionInfo("")
                            .addAllResourceNames(ldsResourceSubscribers.keySet())
                            .setTypeUrl("type.googleapis.com/envoy.config.listener.v3.Listener")
                            .setNode(node)
                            .setResponseNonce("")
                            .build());
        }

    }

    public void sendRdsRequest() {
        if (requestStreamObserver != null) {
            Node node = Node.newBuilder()
                    .setId("b7f9c818-fb46-43ca-8662-d3bdbcf7ec18~10.0.0.1")
                    .setMetadata(Struct.newBuilder().putFields("R_GCP_PROJECT_NUMBER", Value.newBuilder().setStringValue("123456789012").build()).build())
                    .setLocality(Locality.newBuilder().setZone("us-central1-a").build())
                    .setUserAgentVersion("1.41.0")
                    .setUserAgentName("gRPC Java")
                    .addClientFeatures("envoy.lb.does_not_support_overprovisioning")
                    .build();
            Map<String, String> ldsResourceSubscribers = new HashMap<>();
            ldsResourceSubscribers.put("be-srv-route", "be-srv-route");
            requestStreamObserver.onNext(
                    DiscoveryRequest.newBuilder()
                            .setVersionInfo("")
                            .addAllResourceNames(ldsResourceSubscribers.keySet())
                            .setTypeUrl("type.googleapis.com/envoy.config.route.v3.RouteConfiguration")
                            .setNode(node)
                            .setResponseNonce("")
                            .build());
            indicator = 1;
        }
    }

    public void sendCdsRequest() {
        if (requestStreamObserver != null) {
            Node node = Node.newBuilder()
                    .setId("b7f9c818-fb46-43ca-8662-d3bdbcf7ec18~10.0.0.1")
                    .setMetadata(Struct.newBuilder().putFields("R_GCP_PROJECT_NUMBER", Value.newBuilder().setStringValue("123456789012").build()).build())
                    .setLocality(Locality.newBuilder().setZone("us-central1-a").build())
                    .setUserAgentVersion("1.41.0")
                    .setUserAgentName("gRPC Java")
                    .addClientFeatures("envoy.lb.does_not_support_overprovisioning")
                    .build();
            Map<String, String> ldsResourceSubscribers = new HashMap<>();
            ldsResourceSubscribers.put("be-srv-cluster", "be-srv-cluster");
            requestStreamObserver.onNext(
                    DiscoveryRequest.newBuilder()
                            .setVersionInfo("")
                            .addAllResourceNames(ldsResourceSubscribers.keySet())
                            .setTypeUrl("type.googleapis.com/envoy.config.cluster.v3.Cluster")
                            .setNode(node)
                            .setResponseNonce("")
                            .build());
            indicator = 2;
        }
    }

    public void sendCLoadRequest() {
        if (requestStreamObserver != null) {
            Node node = Node.newBuilder()
                    .setId("b7f9c818-fb46-43ca-8662-d3bdbcf7ec18~10.0.0.1")
                    .setMetadata(Struct.newBuilder().putFields("R_GCP_PROJECT_NUMBER", Value.newBuilder().setStringValue("123456789012").build()).build())
                    .setLocality(Locality.newBuilder().setZone("us-central1-a").build())
                    .setUserAgentVersion("1.41.0")
                    .setUserAgentName("gRPC Java")
                    .addClientFeatures("envoy.lb.does_not_support_overprovisioning")
                    .build();
            Map<String, String> ldsResourceSubscribers = new HashMap<>();
            ldsResourceSubscribers.put("be-srv-cluster", "be-srv-cluster");
            requestStreamObserver.onNext(
                    DiscoveryRequest.newBuilder()
                            .setVersionInfo("")
                            .addAllResourceNames(ldsResourceSubscribers.keySet())
                            .setTypeUrl("type.googleapis.com/envoy.config.endpoint.v3.ClusterLoadAssignment")
                            .setNode(node)
                            .setResponseNonce("")
                            .build());
            indicator = 3;
        }
    }
}