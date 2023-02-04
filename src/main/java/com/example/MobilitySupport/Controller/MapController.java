package com.example.MobilitySupport.Controller;


import com.example.MobilitySupport.DTO.Map;
import com.example.MobilitySupport.Service.MapService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Controller
@RequiredArgsConstructor
public class MapController {


    private final MapService mapService;
    // private final ZscodeService zscodeService;




    @GetMapping("/")
    public String list(Model model, @PageableDefault(page= 0, size =10, sort = "id", direction =  Sort.Direction.DESC)Pageable pageable, String searchKeyword) {

        /*
        StringBuffer result = new StringBuffer();

        try {
            String apiUrl = "http://apis.data.go.kr/B552584/EvCharger/getChargerInfo?" +
                    "serviceKey=sOX%2FsqEZDC54GnTToCeYUqGYII9bcjIrgXmvjmjNs%2BhYne2OMkbwhRdyObE7A%2B4%2FMCH1zSb8%2BENu0uYsoPRsBg%3D%3D" +
                    "&numOfRows=9999" +
                    "&pageNo=1"
                    +"&zscode=11110";

            URL url = new URL(apiUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(bufferedInputStream, "UTF-8"));
            String returnLine1;
            while ((returnLine1 = bufferedReader.readLine()) != null) {
                result.append(returnLine1);
            }

            JSONObject jsonObject1 = XML.toJSONObject(result.toString());
            JSONObject response1 = jsonObject1.getJSONObject("response");
            JSONObject body = response1.getJSONObject("body");
            JSONObject items = body.getJSONObject("items");
            //System.out.println(items);
            JSONArray item = items.getJSONArray("item");

            mapService.updateData(item);
        } catch (Exception e) {
            e.printStackTrace();
        }

         */
        List<Map> mapData = null;
        Page<Map> list = null;
        if(searchKeyword == null) {
            list = mapService.findByAddrContaining(pageable, "서울");
            mapData = mapService.findByChargingStation("서울");
        }else {
            list = mapService.findByAddrContaining(pageable, searchKeyword);
            mapData = mapService.findByChargingStation(searchKeyword);

        }
        System.out.println(searchKeyword+"키워드");

        int nowPage = list.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, list.getTotalPages());

        model.addAttribute("list", list);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("mapData", mapData);

        return "/map/map";
    }


    @GetMapping("/mapDataInsert")
    public String mapDataInsert(HttpServletResponse response) {
        //mapService.delete();
        StringBuffer result = new StringBuffer();
        try {
            String apiUrl = "http://apis.data.go.kr/B552584/EvCharger/getChargerInfo?" +
                    "serviceKey=sOX%2FsqEZDC54GnTToCeYUqGYII9bcjIrgXmvjmjNs%2BhYne2OMkbwhRdyObE7A%2B4%2FMCH1zSb8%2BENu0uYsoPRsBg%3D%3D" +
                    "&numOfRows=9999" +
                    "&pageNo=1";


            URL url = new URL(apiUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(bufferedInputStream, "UTF-8"));
            String returnLine;
            while ((returnLine = bufferedReader.readLine()) != null) {
                result.append(returnLine);
            }

            JSONObject jsonObject = XML.toJSONObject(result.toString());
            JSONObject response1 = jsonObject.getJSONObject("response");
            JSONObject body = response1.getJSONObject("body");
            JSONObject items = body.getJSONObject("items");
            JSONArray item = items.getJSONArray("item");
            mapService.save(item);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }

}



