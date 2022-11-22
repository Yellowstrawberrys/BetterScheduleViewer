package me.yellowstrawberry.ystschoolhelper;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.text.DateFormatter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

@RestController
@RequestMapping("/contents")
public class ContentsHandler {

    String format = """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <title>Contents</title>
                <link rel="stylesheet" href="/timeline.css">
                <script src="/timeline.js"></script>
            </head>
                <body>
                    <div id="one">
                        %s
                    </div>
                    <canvas id="two"></canvas>
                    <div id="three">
                        %s
                    </div>
                </body>
            </html>""";

    String format1 = """
            <div class="scheduleContainer">
                <p class="scheduleTitle">%s</p>
                <p class="scheduleDescription">기타 정보 없음</p>
            </div>""";

    String format2 = """
            <div class="time">
                <p class="timeText1">%s 교시</p>
                <p class="timeText2">(N/A)</p>
            </div>""";
    OkHttpClient client = new OkHttpClient();
    final Map<String, String> emojis = Map.ofEntries(
            entry("국어", "\uD83D\uDCDD "),
            entry("수학", "📊 "),
            entry("사회", "🏢 "),
            entry("과학", "\uD83E\uDDEA "),
            entry("화학", "⚗ "),
            entry("물리", "\uD83D\uDD28 "),
            entry("영어", "🌏 "),
            entry("역사", "📜 "),
            entry("체육", "🏃 "),
            entry("한문", "🈯 "),
            entry("미술", "🎨 "),
            entry("음악", "🎵 "),
            entry("정보", "💻 "),
            entry("진로", "‍💼 "),
            entry("도덕", "‍❤ "),
            entry("독일어", "\uD83C\uDDE9\uD83C\uDDEA "),
            entry("프랑스어", "\uD83C\uDDEB\uD83C\uDDF7 "),
            entry("러시아어", "\uD83C\uDDF7\uD83C\uDDFA "),
            entry("스페인어", "\uD83C\uDDEA\uD83C\uDDF8 "),
            entry("아랍어", "\uD83C\uDDE6\uD83C\uDDEA "),
            entry("베트남어", "\uD83C\uDDFB\uD83C\uDDF3 "),
            entry("일본어", "\uD83C\uDDEF\uD83C\uDDF5 "),
            entry("중국어", "\uD83C\uDDE8\uD83C\uDDF3 "),
            entry("기술", "\uD83E\uDE84 "),
            entry("가정", "\uD83E\uDDF0 "),
            entry("바른생활", "🌳 "),
            entry("슬기로운생활", "🌳 "),
            entry("자율", "\uD83C\uDD93 ")
    );

    @RequestMapping("/")
    public String onRequest(@RequestParam("SN") String sn, @RequestParam("G") String g, @RequestParam("C") String c, @RequestParam(value = "date", required = false) String date) {
        try {
            Map<String, String> map = getInformation(sn, g, c, date);
            return format.formatted(map.get("Period"), map.get("Schedule"));
        }catch (NullPointerException e) {
            return "해당 학교는 존재하지 않습니다.";
        }
    }

    @RequestMapping("/na")
    public String onRequestNA() {
        return "<p>학교를 검색 해주세요^^</p>";
    }

    public String getEmoji(String key) {
        if(emojis.containsKey(key)){
            return emojis.get(key);
        }else {
            for(String st : emojis.keySet()) {
                if(key.contains(st)){
                    return emojis.get(st);
                }
            }
        }
        return "❓ ";
    }

    public Map<String, String> getInformation(String sn, String g, String c, String date) throws NullPointerException {
        Map<String, String> map = new HashMap<>();

        Request schoolInformationRequest = new Request.Builder()
                .url("https://open.neis.go.kr/hub/schoolInfo?KEY=%s&Type=json&pIndex=1&pSize=5&SCHUL_NM=%s".formatted(YstSchoolHelperApplication.apiKey1, sn))
                .get()
                .build();
        try (Response response = client.newCall(schoolInformationRequest).execute()) {
            JSONObject obj = new JSONObject(response.body().string()).getJSONArray("schoolInfo").getJSONObject(1).getJSONArray("row").getJSONObject(0);
            JSONObject schedule = getTimeSchedule(
                    (obj.getString("SCHUL_KND_SC_NM").equals("초등학교") ? "els"
                                    : (obj.getString("SCHUL_KND_SC_NM").equals("중학교") ? "mis" : "his")),
                    obj.getString("ATPT_OFCDC_SC_CODE"),
                    obj.getString("SD_SCHUL_CODE"),
                    g,
                    c,
                    (date != null ? date : new SimpleDateFormat("yyyyMMdd").format(new Date())),
                    (date != null ? date : new SimpleDateFormat("yyyyMMdd").format(new Date()))
            );

            String st = "";
            String st1 = "";
            for (Object jsonObject : schedule.getJSONArray((obj.getString("SCHUL_KND_SC_NM").equals("초등학교") ? "els"
                    : (obj.getString("SCHUL_KND_SC_NM").equals("중학교") ? "mis" : "his"))+"Timetable").getJSONObject(1).getJSONArray("row")) {
                JSONObject jsObj = new JSONObject(jsonObject.toString());
                String name = jsObj.getString("ITRT_CNTNT").substring((obj.getString("SCHUL_KND_SC_NM").equals("중학교") ? 1 : 0)).replaceFirst("활동", "");
                st += format1.formatted(getEmoji(name)+name);
                st1 += format2.formatted(jsObj.getString("PERIO"));
            }
            map.put("Schedule", st);
            map.put("Period", st1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return map;
    }

    public JSONObject getTimeSchedule(String prefix, String ATPT_OFCDC_SC_CODE, String SD_SCHUL_CODE, String GRADE, String CLASS_NM, String TI_FROM_YMD, String TI_TO_YMD) {
        Request scheduleRequest = new Request.Builder()
                .url("https://open.neis.go.kr/hub/%sTimetable?KEY=%s&Type=json&pIndex=1&pSize=100&ATPT_OFCDC_SC_CODE=%s&SD_SCHUL_CODE=%s&GRADE=%s&CLASS_NM=%s&TI_FROM_YMD=%s&TI_TO_YMD=%s"
                        .formatted(prefix, YstSchoolHelperApplication.apiKey1, ATPT_OFCDC_SC_CODE, SD_SCHUL_CODE, GRADE, CLASS_NM, TI_FROM_YMD, TI_TO_YMD)
                ).get()
                .build();
        try (Response response = client.newCall(scheduleRequest).execute()) {
            return new JSONObject(response.body().string());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
