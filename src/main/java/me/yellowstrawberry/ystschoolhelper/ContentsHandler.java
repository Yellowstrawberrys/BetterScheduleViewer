package me.yellowstrawberry.ystschoolhelper;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/contents/")
public class ContentsHandler {

    String format = """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <title>Contents</title>
                <link rel="stylesheet" href="timeline.css">
                <script src="timeline.js"></script>
            </head>
                <body>
                    <div id="one">
                        <div class="time">
                            <p class="timeText1">1 교시</p>
                            <p class="timeText2">(9:05 ~ 9:55)</p>
                        </div>
                        %s
                    </div>
                    <canvas id="two"></canvas>
                    <div id="three">
                        <div class="scheduleContainer">
                            <p class="scheduleTitle">📘 수학</p>
                            <p class="scheduleDescription">기타 정보 없음</p>
                        </div>
                        %s
                    </div>
                </body>
            </html>""";
    OkHttpClient client = new OkHttpClient();

    @RequestMapping("/")
    public String onRequest(@RequestParam("SN") String sn, @RequestParam("G") String g, @RequestParam("C") String c) {
        return format.formatted()
    }

    public String getTimeSchedule(String sn, String g, String c) {
        Request schoolInformation = new Request.Builder()
                .url("https://open.neis.go.kr/hub/schoolInfo?KEY=%s&Type=json&pIndex=1&pSize=5&SCHUL_NM=%s".formatted("", ""))
                .get()
                .build();

    }
}
