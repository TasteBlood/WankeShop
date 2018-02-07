package com.cloudcreativity.wankeshop.userCenter.address;

import com.cloudcreativity.wankeshop.entity.address.AreaEntity;
import com.cloudcreativity.wankeshop.entity.address.CityEntity;
import com.cloudcreativity.wankeshop.entity.address.ProvinceEntity;
import com.cloudcreativity.wankeshop.entity.address.StreetEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 用来保存临时的数据
 */
public class TempAddress {
    public static List<ProvinceEntity> provinceEntities = new ArrayList<>();
    public static List<CityEntity> cityEntities = new ArrayList<>();
    public static List<AreaEntity> areaEntities = new ArrayList<>();
    public static List<StreetEntity> streetEntities = new ArrayList<>();

    public static ProvinceEntity provinceEntity;
    public static CityEntity cityEntity;
    public static AreaEntity areaEntity;
    public static StreetEntity streetEntity;

    public static void clear(){
        provinceEntities = null;
        cityEntities = null;
        areaEntities = null;
        streetEntities = null;
        provinceEntity = null;
        cityEntity = null;
        areaEntity = null;
        streetEntity = null;
    }
}
