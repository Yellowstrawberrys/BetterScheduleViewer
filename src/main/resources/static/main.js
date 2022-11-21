let date;
let text;
let contents;
let dayString = ['월', '화', '수', '목', '금'];

function init() {
    date = new Date();
    text = document.getElementById('day');
    contents = document.getElementById('contents');
}

function left_b() {
    oneDayOff();
    text.textContent = date.getFullYear()+'. '+(date.getMonth()+1)+'. '+date.getDate()+'. ('+dayString[date.getDay()-1]+')';
    search();
}

function right_b() {
    oneDayOn();
    text.textContent = date.getFullYear()+'. '+(date.getMonth()+1)+'. '+date.getDate()+'. ('+dayString[date.getDay()-1]+')';
    search();
}

function oneDayOff() {
    let day = new Date(date.getFullYear(), date.getMonth(), date.getDate()-1);
    if(day.getDay() < 1 || day.getDay() > 5) {
        day = new Date(date.getFullYear(), date.getMonth(), day.getDate()-(day.getDay() === 0 ? 2 : 1));
    }
    date = day;
}

function oneDayOn() {
    let day = new Date(date.getFullYear(), date.getMonth(), date.getDate()+1);
    if(day.getDay() < 1 || day.getDay() > 5) {
        day = new Date(date.getFullYear(), date.getMonth(), day.getDate()+(day.getDay() === 0 ? 1 : 2));
    }
    date = day;
}

function search() {
    if(document.getElementById('searchInput') != null && document.getElementById('gradeInput') != null && document.getElementById('classInput') != null){
        contents.src = '/contents/?SN='+document.getElementById('searchInput').value+'&G='+document.getElementById('gradeInput').value+'&C='+document.getElementById('classInput').value+'&date='+getFormattedDate();
    }
}

function getFormattedDate() {
    return date.getFullYear()+((date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : (date.getMonth()+1)+''))+(date.getDate() < 10 ? '0'+date.getDate() : date.getDate()+'');
}

window.addEventListener('DOMContentLoaded', (event) => {
    init();
});