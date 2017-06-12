package edu.dsiedlarz.ParkingMeterAssistant.api.soap.external;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * Created by private on 03.06.2017.
 */
@WebService
public interface PlacesExternalApi {
    @WebMethod
    String getStats();

    @WebMethod
    String getStatsByLocation(int id);

    @WebMethod
    String getPlaces();

    @WebMethod
    String getLocationPlaces(int id);
}



