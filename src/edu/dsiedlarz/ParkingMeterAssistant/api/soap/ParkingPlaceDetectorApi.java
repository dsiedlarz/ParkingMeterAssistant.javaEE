package edu.dsiedlarz.ParkingMeterAssistant.api.soap;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * Created by private on 03.06.2017.
 */
@WebService
public interface ParkingPlaceDetectorApi {
    @WebMethod
    boolean tookPlace(@WebParam(name = "id")int id);
    @WebMethod
    boolean freePlace(@WebParam(name = "id")int id);
}


