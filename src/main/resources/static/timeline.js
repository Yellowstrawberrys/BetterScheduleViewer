let canvas;
let ctx;
let height;
let currentScheduleLevel = 1;
let schedules;

function init() {
    canvas = document.getElementById("two");
    ctx = canvas.getContext("2d");

    height = getDocHeight() * 2;
    canvas.width = getDocWidth()*0.08 * 2;
    canvas.height = height;
    schedules = document.getElementsByClassName('time').length;

    // adjustLocations();

    ctx.lineWidth = 5;
    animateLines(0);
}

function adjustLocations() {
    let i = 1;
    for (const time of document.getElementsByClassName('time')) {
        time.style.marginTop = ((document.body.offsetHeight / (schedules+1)) - (time.offsetHeight / 2) - (i > 1 ? time.offsetHeight*0.4266666666666667 : 0))+ 'px';
        i++;
    }
}

function adjustObject(i, y) {
    let time = document.getElementsByClassName('time')[i];
    time.style = 'position: absolute; top: ' + (y-(time.offsetHeight/2)) + 'px; left: 5%;';
    let scheduleContainer = document.getElementsByClassName('scheduleContainer')[i];
    scheduleContainer.style = 'position: absolute; top: ' + (y-(scheduleContainer.offsetHeight/2)) + 'px;';
    time.style.visibility = 'visible';
    time.style.animation = '2s 1 splash';
    scheduleContainer.style.visibility = 'visible';
    scheduleContainer.style.animation = '2s splash';
}

function animateLines(percentage) {
    let dy = ((height/(schedules+1)) + 15) * (currentScheduleLevel-1);
    drawLine(25, ((height/(schedules+1)) - 15), dy, percentage);
    drawLine(40, ((height/(schedules+1)) - 15), dy, percentage);
    if(percentage < 1.01) {
        requestAnimationFrame(function() {
            animateLines(percentage + 0.01);
        });
    }else if (currentScheduleLevel < (schedules+1)) {
        currentScheduleLevel++;
        animateCircles(0);
        animateSideLines(0);
        animateLines(0);
    }
}

function animateCircles(percentage) {
    let y = (height/(schedules+1))*(currentScheduleLevel-1) + 15 * (currentScheduleLevel-2);
    drawCircle(y, percentage);
    if(percentage < 1.01) {
        requestAnimationFrame(function() {
            animateCircles(percentage + 0.01);
        });
    }else {
        adjustObject(currentScheduleLevel-2, y/2);
    }
}

function animateSideLines(percentage) {
    drawSideLine(canvas.width-47.5, 47.5, (height/(schedules+1))*(currentScheduleLevel-1) + 15 * (currentScheduleLevel-2), percentage);
    if(percentage < 1.01) {
        requestAnimationFrame(function() {
            animateSideLines(percentage + 0.01);
        });
    }
}

function drawCircle(y, alpha) {
    ctx.strokeStyle = 'rgba(255,174,0,'+alpha+')';
    ctx.fillStyle = 'rgba(38,38,38,1)';
    ctx.beginPath();
    ctx.arc(32.5, y, 15, 0, 2 * Math.PI);
    ctx.fill();
    ctx.stroke();
}

function drawLine(x, y, dy, percentage) {
    ctx.strokeStyle = '#FFAE00';
    ctx.beginPath();
    ctx.moveTo(x, dy);
    ctx.lineTo(x, dy+(y*percentage));
    ctx.stroke();
}

function drawSideLine(x, dx, y, percentage) {
    ctx.strokeStyle = '#CD8F09';
    ctx.beginPath();
    ctx.moveTo(dx, y);
    ctx.lineTo(dx+(x*percentage), y);
    ctx.stroke();
}

function getDocWidth() {
    let body = document.body,
        html = document.documentElement;

    return Math.max(body.scrollWidth, body.offsetWidth,
        html.clientWidth, html.scrollWidth, html.offsetWidth);
}

function getDocHeight() {
    let body = document.body,
        html = document.documentElement;

    return Math.max(body.scrollHeight, body.offsetHeight,
        html.clientHeight, html.scrollHeight, html.offsetHeight);
}

window.addEventListener('DOMContentLoaded', (event) => {
    init();
});