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

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;


@Slf4j
@Controller
@RequiredArgsConstructor
public class MapController {


    private final MapService mapService;
    // private final ZscodeService zscodeService;


    @GetMapping("/")
    public String map2(HttpServletResponse response) {
        return "/map/map";
    }

    @PostMapping("/")
    public String map(HttpServletResponse response) {
        return "/map/map";
    }

    @GetMapping("/list")
    public String list(Model model, @RequestParam(required = false, defaultValue = "0", value = "page") int page) {
        Long zcode = Long.valueOf(11);
        Page<Map> listPage = mapService.findByZcode(page, zcode); // 불러올 페이지의 데이터 1페이지는 0부터 시작

        int totalPage = listPage.getTotalPages(); // 총 페이지 수

        model.addAttribute("board", listPage.getContent());
        model.addAttribute("totalPage", totalPage);

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
/*
    @PostMapping("/update")
    public String update(HttpServletResponse response) {
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

            mapService.updateData(item);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }




    @ResponseBody
    @RequestMapping(value = "/statusWindow", method = RequestMethod.POST)
    public List<Map> mapDataInsert(@RequestParam("statid") String stat_id, @RequestParam("zcode") Long zcode, String lng, HttpServletResponse response) {
        //mapService.delete();
        List<Map> status = new ArrayList<>();
        StringBuffer result = new StringBuffer();
        try {
            String apiUrl = "http://apis.data.go.kr/B552584/EvCharger/getChargerInfo?" +
                    "serviceKey=sOX%2FsqEZDC54GnTToCeYUqGYII9bcjIrgXmvjmjNs%2BhYne2OMkbwhRdyObE7A%2B4%2FMCH1zSb8%2BENu0uYsoPRsBg%3D%3D" +
                    "&numOfRows=9999" +
                    "&pageNo=1"
                    + "&zscode=" + zcode;


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
            for (int i = 0; i < item.length(); i++) {
                if (!(((String) item.getJSONObject(i).get("statId")).equals(stat_id))) {
                    continue;
                }
                Map map = new Map();
                map.setChgerId(String.valueOf(item.getJSONObject(i).get("chgerId")));
                map.setStatId((String) item.getJSONObject(i).get("statId"));
                if (item.getJSONObject(i).get("addr") == JSONObject.NULL) {
                    map.setAddr("");
                } else {
                    map.setAddr((String) item.getJSONObject(i).get("addr"));
                }
                map.setStat(((Number) item.getJSONObject(i).get("stat")).longValue());
                map.setZcode(((Number) item.getJSONObject(i).get("zcode")).longValue());
                map.setLat(Double.parseDouble(String.valueOf(item.getJSONObject(i).get("lat"))));
                map.setLng(Double.parseDouble(String.valueOf(item.getJSONObject(i).get("lng"))));
                System.out.println(map.getAddr());
                System.out.println(map.getStat());
                System.out.println(map.getStatId());
                System.out.println(map.getLng());
                status.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }
*/


    @RequestMapping(value = "/address", method = RequestMethod.POST)
    public String data(Model model, @RequestParam("lng") String lng, @RequestParam("lat") String lat, HttpServletResponse response) {
        String REST_KEY = "9f023ef16b3fb6d5c62b7032504394d0";

        String GEOCODE_URL = "https://dapi.kakao.com/v2/local/geo/coord2regioncode.json?";
        String GEOCODE_USER_INFO = "KakaoAK 9f023ef16b3fb6d5c62b7032504394d0";



       // https://developers.kakao.com/docs/latest/ko/local/dev-guide#coord-to-address

        URL obj;
        try {
            String coordinatesystem = "WGS84";
            obj = new URL(GEOCODE_URL + "x=" + lng + "&y=" + lat + "&input_coord=" + coordinatesystem);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", GEOCODE_USER_INFO);
            con.setRequestProperty("content-type", "application/json");
            con.setDoOutput(true);
            con.setUseCaches(false);
            Charset charset = Charset.forName("UTF-8");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), charset));
            StringBuffer result = new StringBuffer();
            String returnLine;
            while ((returnLine = in.readLine()) != null) {
                result.append(returnLine);
            }
            JSONObject jsonObject = new JSONObject(result.toString());

            String code = jsonObject.getJSONArray("documents").getJSONObject(0).getString("code");
            System.out.println(code+"얏호");
            try {
                String apiUrl = "http://apis.data.go.kr/B552584/EvCharger/getChargerInfo?" +
                        "serviceKey=sOX%2FsqEZDC54GnTToCeYUqGYII9bcjIrgXmvjmjNs%2BhYne2OMkbwhRdyObE7A%2B4%2FMCH1zSb8%2BENu0uYsoPRsBg%3D%3D" +
                        "&numOfRows=9999" +
                        "&pageNo=1"
                        +"&zscode=43110";

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


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
        return null;
    }

    @ResponseBody
    @RequestMapping(value = "/apitest", method = RequestMethod.POST)
    public List<Map> findByChargingStation(@RequestParam("lat") String lat, @RequestParam("lng") String lng) {
        //System.out.println(lat + "---" + lng);

        List<Map> mapData = mapService.findByChargingStation(Long.valueOf(11));
        //System.out.print(mapData.size());
        return mapData;
    }


}




