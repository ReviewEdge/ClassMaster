import { setCookie, getCookie } from './useCookies.js';
import { getCurrentSchedule } from './schedule.js';


const daysOfWeek = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday'];
const startHour = 8;
let endHour = 16;

let events = [];


async function scheduleToListOfClasses(schedule) {
    events = []

    // console.log("hey");

    // console.log(schedule);

    const result = await schedule

    // console.log(result);
    // console.log("promise recieved")

    result.classes.forEach(element => {
        const newEvent = { day: '', start: { hour: 0, minute: 0 }, end: { hour: 0, minute: 0 }, title: '' }
        newEvent.title = element.courseCodeWithoutTerm;

        element.timeSlots.forEach(element => {
            const instanceOfEvent = JSON.parse(JSON.stringify(newEvent))
            // console.log(instanceOfEvent)
            const startSplit = element.start.split(":");
            const endSplit = element.end.split(":");
            
            instanceOfEvent.start.hour = parseInt(startSplit[0]);
            instanceOfEvent.start.minute = parseInt(startSplit[1]);
            
            instanceOfEvent.end.hour = parseInt(endSplit[0]);
            instanceOfEvent.end.minute = parseInt(endSplit[1]);

            //extend the day if there is a late class
            if(instanceOfEvent.end.hour > 16) {
                endHour = 20;
            }

            instanceOfEvent.day = element.day;
            events.push(instanceOfEvent);
        });
    });
    // console.log(events)
    generateCalendar();

}


function generateCalendar() {
    const calendarEl = document.getElementById('calendar');
    calendarEl.innerHTML = ''

    // Add days of the week
    daysOfWeek.forEach(day => {
        const dayEl = document.createElement('div');
        dayEl.classList.add('day');
        dayEl.textContent = day;

        // Add hours
        for (let i = startHour; i <= endHour; i++) {
            const hourEl = document.createElement('div');
            hourEl.classList.add('hour');

            // use 12 hour time

            let tweHour = i;
            let meridian = " AM"

            if (tweHour > 12) {
                tweHour = tweHour-12;
                meridian = " PM";
            }

            hourEl.textContent = i + meridian;
            dayEl.appendChild(hourEl);
        }

        calendarEl.appendChild(dayEl);

    });



    // Add events
    events.forEach(event => {
        // console.log(event);

        // Calculate position and size of event
        const topOfDiv = 23.7;
        const heightOfOneHour = 29.22;


        const dayIndex = daysOfWeek.indexOf(event.day);
        // const start = ((event.start.hour - startHour) * 60 + event.start.minute) / 60 + 1;
        // const end = ((event.end.hour - startHour) * 60 + event.end.minute) / 60 + 1;
        const duration = (event.end.hour + (event.end.minute/60)) - (event.start.hour + (event.start.minute/60));
        // const top = (start - 1) * 60 + (event.start.minute || 0);
        const top = topOfDiv + ((event.start.hour-startHour) * heightOfOneHour) + ((event.start.minute / 60) * heightOfOneHour);
        const height = duration * heightOfOneHour;

        // console.log(duration);

        // console.log(event.title);
        // console.log(dayIndex);
        // console.log(start);
        // console.log(end);
        // console.log(duration);
        // console.log(top);
        // console.log(height);


        // Create event element
        const eventEl = document.createElement('div');
        eventEl.classList.add('event');
        eventEl.style.top = top + 'px';
        eventEl.style.height = height + 'px';
        eventEl.textContent = event.title;

        // console.log(eventEl.style);

        // Add event to calendar
        // console.log(calendarEl.children);
        const cellEl = calendarEl.children[dayIndex];
        cellEl.appendChild(eventEl);
    });
}


export {scheduleToListOfClasses as refreshCalendar}
