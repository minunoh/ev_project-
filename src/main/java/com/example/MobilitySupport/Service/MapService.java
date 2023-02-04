package com.example.MobilitySupport.Service;


import com.example.MobilitySupport.DTO.Map;
import com.example.MobilitySupport.repository.TestRepository;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.jar.JarException;


@Service
@Transactional
public class MapService {

    private final TestRepository testRepository;


    public MapService(TestRepository mapRepository) {
        this.testRepository = mapRepository;
    }


    //https://kim-jong-hyun.tistory.com/32     :  Json
    /*
        회원가입
         */
    public void save(JSONArray jsonArray) {


        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                Map map = new Map();
                map.setChgerId(String.valueOf(jsonArray.getJSONObject(i).get("chgerId")));
                map.setStatId((String) jsonArray.getJSONObject(i).get("statId"));
                if (jsonArray.getJSONObject(i).get("addr") == JSONObject.NULL) {
                    map.setAddr("");
                } else {
                    map.setAddr((String) jsonArray.getJSONObject(i).get("addr"));
                }

                map.setStat(((Number) jsonArray.getJSONObject(i).get("stat")).longValue());
                map.setZcode(((Number) jsonArray.getJSONObject(i).get("zcode")).longValue());

                map.setLat(Double.parseDouble(String.valueOf(jsonArray.getJSONObject(i).get("lat"))));
                map.setLng(Double.parseDouble(String.valueOf(jsonArray.getJSONObject(i).get("lng"))));
                System.out.println(map.getAddr());
                System.out.println(map.getStat());
                System.out.println(map.getStatId());
                System.out.println(map.getLng());
                testRepository.save(map);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }


    public void delete() {
        testRepository.deleteAll();
    }


    public void updateData(JSONArray jsonArray) throws JSONException {
        try{
            for (int i = 0; i < jsonArray.length(); i++) {
                Long state = ((Number) jsonArray.getJSONObject(i).get("stat")).longValue();
                String chgerId = String.valueOf(jsonArray.getJSONObject(i).get("chgerId"));
                String statId = String.valueOf(jsonArray.getJSONObject(i).get("statId"));

                testRepository.updateMapData(state, chgerId, statId);
            }
        }

        catch (JSONException e){
            throw new RuntimeException(e);
        }

    }


    public Page<Map> findByAddrContaining(Pageable pageable, String addr) {
        return testRepository.findByAddrContaining(addr, pageable);

    }

    public Page<Map> getBoardList(Pageable pageable,String addr) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "id")); // <- Sort 추가

        return testRepository.findByAddrContaining(addr, pageable);
    }

    public List<Map> findByAll() {
        return testRepository.findAll();
    }

    public List<Map> findByChargingStation(String addr) {
        return testRepository.findByAddrContaining(addr);
    }



}
