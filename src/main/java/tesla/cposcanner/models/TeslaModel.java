package tesla.cposcanner.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class TeslaModel {
	private String ConfigId;
    private String OptionCodeList;
    private String TitleStatus;
    private String TitleSubStatus;
    private String VehicleStatus;
    private int Odometer;
    private int UsedVehiclePrice;
    private String OptionCodeListWithPrice;
    private String DestinationHandlingFee;
    private String TradeInType;
    private String Discount;
    private String Vin;
    private int Year;
    private String OdometerType;
    private String OwnerShipTransferCount;
    private String Model;
    private String ModelVariant;
    private String DriveTrain;
    private String Paint;
    private String Badge;
    private String Battery;
    private String Range;
    private boolean isFirstRegistrationDate;
    private String Wheel;
    private String AutoPilot;
    private String Decor;
    private String Roof;
    private int MetroId;
    private boolean isPremium;
    private boolean isPanoramic;
    private boolean isAutopilot;
    private boolean isFixedGlassRoof;
    
}
