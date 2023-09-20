//  least connection, round robin...etc 

/*
 * client will come to this factory and will say I want this algorithm 
 * ans we will have to give him that load balancer 
 */

 /*
   Load balancer - what it do?? it will have Request as input ---> Node as o/p, so basically routing of requests
  */

//  abstract class - so it means we cannot create a load balancer. We will always say we want this type of load balancer

 abstract class LoadBalancer{
    /*
      we need set of nodes for set of destinations for a type of request
      like for profile request--- these are the sets of destinations/servers

      Any type of request will have its request type

      example... suppose there is a profile request, profile service will say I have say 6 nodes, and you have to route in 1 of these nodes

     */

    // Map<RequestType, Set<Destination>>; 

    /*
     * one type of request is accepeted by multple services?? not possible
     * one type of service can accept multple request type
     */
    Map<RequestType, Service> serviceMap;

    //There should be a way to register a request type to a service
    public void register(RequestType requestType, Service){
        serviceMap.put(requestType, service);
    }

    protected Set<Destination> getDestinations(Request request){
        Service service = serviceMap.get(request.requestType);
        return service.destinations;
    }

    abstract Destination balanceLoad(Request request);
 }

 class LeastConnectionLoadBalancer extends LoadBalancer{

    /* 
     * if I am a load balancer, what should I know:
     * 1. which destinations I can route something to
     * 2. what type of requests are coming in
     * 3. and to know how I will route this?
     */

     /*
        * Question
        You are balancing the load here where you are accessing destinations
        Service has a list of destinations
        So why not give this responsibility to Service to give the correct destination and then you can just return it.

        We don't do here because LeastConnection Load balancer has a particular type of login which Service should not be aware of.

        Like if I tell, give me the one minimum connections,the service has nothing to do with it. As a service as an object as an abstract concept, it is wierd that I'm asking it to give me a destination with minimum number of connections. 
     */
     
     @Override
     Destination balanceLoad(Request request){
       
        //for every destination, I need to checkc which has the lowest load - minimum number of connections
        getDestinations(request).stream()
            .min(Comparator.comparingInt(d->d.requestsBeingServed))
            .orElseThrow();
        return destination;

     }

 }

 class RoundRobinLoadBalancer extends LoadBalancer{
    
    Map<RequestType, Queue<Destination>> destinationsForRequest;

    @Override
    Destination balanceLoad(Request request){
        if(!destinationsForRequest.containsKey(request.requestType)){
            Set<Destination> destinations = getDestinations(request);
            destinationsForRequest.put(request.requestType, convertToQueue(destinations));
        }
        Destination destination = destinationsForRequest.get(request.requestType).poll();
        destinationsForRequest.get(request.requestType).add(destination);

        return destination;
    }
 }
 // to handle sticky sessions

 class RoutedLoadBalancer extends LoadBalancer{
    
    @Override
    Destination balanceLoad(Request request){
        Set<Destination> destinations = getDestinations(request);
        List<Destination> collect = destinations.stream().collect(Collectors.toList());
        return list.get(request.id.hashCode() % list.size());
 }
}

/*
 * In service, can we change anything?? No, this class is immutable
 * 
 * There should be a way to add destinations to a service
 */
 class Service{
    String name;
    Set<Destination> destinations;

    public void addDestination(Destination destination){
         destinations.add(destination);
    }
    public void removeDestination(Destination destination){
        destinations.remove(destination);
   }
 }

 class Request{

    String id;
    RequestType requestType;
    Map<String, String> parameters;
 }

 enum{}

 class Destination{
    String ipAddress;
    // double weight;
    int requestsBeingServed;
    int threshold;

    // void incrementNumberOfRequests(){
    //     requestsBeingServed++;
    // }

    public boolean acceptRequest(Request request){
        if(threshold <= requestsBeingServed){
            requestsBeingServed++;
            return true;
        }
        else{
            return false;
        }
    }

    /*
     * destination may call this method in itself, so private method completeRequest which will decrement once request has been served or completed
     */
    private void completeRequest(){
        requestsBeingServed--;
    }
 }

class LoadBalancerFactory{
    public LoadBalancer createLoadBaalancer(String lbType) {
        return  switch(lbType){
            case "round-robin": new RoundRobinLoadBalancer();
            case "least-connection": new LeastConnectionLoadBalancer();
            default: new LeastConnectionLoadBalancer();
        }
    }
}


// When load balancer actually assigns a node, it will change the state of that node(i.e., destination)
// It will have a threshold as well. Threshold can be in terms of weight as well
// If you are balancing load, and threshold be like 100 requests/service
 
 
 


// -----------------------------code ----------------------------------

abstract class LoadBalancer{
   
    Map<RequestType, Service> serviceMap;
    
    public void register(RequestType requestType, Service){
        serviceMap.put(requestType, service);
    }

    protected Set<Destination> getDestinations(Request request){
        Service service = serviceMap.get(request.requestType);
        return service.destinations;
    }

    abstract Destination balanceLoad(Request request);
 }

 class LeastConnectionLoadBalancer extends LoadBalancer{
     
     @Override
     Destination balanceLoad(Request request){
        getDestinations(request).stream()
            .min(Comparator.comparingInt(d->d.requestsBeingServed))
            .orElseThrow();
        return destination;

     }

 }

 class RoundRobinLoadBalancer extends LoadBalancer{
    
    Map<RequestType, Queue<Destination>> destinationsForRequest;

    @Override
    Destination balanceLoad(Request request){
        if(!destinationsForRequest.containsKey(request.requestType)){
            Set<Destination> destinations = getDestinations(request);
            destinationsForRequest.put(request.requestType, convertToQueue(destinations));
        }
        Destination destination = destinationsForRequest.get(request.requestType).poll();
        destinationsForRequest.get(request.requestType).add(destination);
        destination.acceptRequest(request);

        return destination;
    }
 }

 class RoutedLoadBalancer extends LoadBalancer{
    
    @Override
    Destination balanceLoad(Request request){
        Set<Destination> destinations = getDestinations(request);
        List<Destination> collect = destinations.stream().collect(Collectors.toList());
        return list.get(request.id.hashCode() % list.size());
 }
}

 class Service{
    String name;
    Set<Destination> destinations;

    public void addDestination(Destination destination){
         destinations.add(destination);
    }
    public void removeDestination(Destination destination){
        destinations.remove(destination);
   }
 }

 class Request{

    String id;
    RequestType requestType;
    Map<String, String> parameters;
 }

 enum RequestType{

 }

 class Destination{
    String ipAddress;
    int requestsBeingServed;
    int threshold;

    public boolean acceptRequest(Request request){
        if(threshold <= requestsBeingServed){
            requestsBeingServed++;
            return true;
        }
        else{
            return false;
        }
    }
    
    private void completeRequest(){
        requestsBeingServed--;
    }
 }


class LoadBalancerFactory{
    public LoadBalancer createLoadBaalancer(String lbType) {
        return  switch(lbType){
            case "round-robin": new RoundRobinLoadBalancer();
            case "least-connection": new LeastConnectionLoadBalancer();
            default: new LeastConnectionLoadBalancer();
        }
    }
}
 