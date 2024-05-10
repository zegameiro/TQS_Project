import { useState } from 'react'
import './App.css'

import sound from './assets/call.mp3'

import { Table, TableHeader, TableColumn, TableBody, TableRow, TableCell, getKeyValue } from "@nextui-org/react";

function App() {

  function playSound() {
    const audio = new Audio(sound)
    audio.play()
  }

  const monthNames = [
    'January', 'February', 'March', 'April', 'May', 'June',
    'July', 'August', 'September', 'October', 'November', 'December'
  ];

  const columns = [
    {
      label: "section",
    },
    {
      label: "chair",
    },
    {
      label: "name",
    },
  ];


  const reservartions = [
    { name: 'John Doe', onQueue: false, section: 'A', chair: 1 },
    { name: 'Jane Doe', onQueue: false, section: 'B', chair: 4 },
    { name: 'John Smith', onQueue: false, section: 'C', chair: 3 },
    { name: 'Jane Smith', onQueue: false, section: 'D', chair: 2 },
    { name: 'Brian Connor', onQueue: true, section: 'E', chair: null },
    { name: 'Sarah Connor', onQueue: false, section: 'F', chair: 3 },
    { name: 'John Wick', onQueue: true, section: 'G', chair: null },
    { name: 'Jane Wick', onQueue: false, section: 'H', chair: 4 },
    { name: 'John Rambo', onQueue: true, section: 'B', chair: null },
    { name: 'Jane Rambo', onQueue: false, section: 'C', chair: 2 },
    { name: 'John McClane', onQueue: true, section: 'D', chair: null },
    { name: 'Jane McClane', onQueue: false, section: 'E', chair: 3 },
    { name: 'John Matrix', onQueue: true, section: 'A', chair: null },
    { name: 'Jane Matrix', onQueue: false, section: 'B', chair: 1 },
  ]

  const onGoingReservations = reservartions.filter((reservation) => !reservation.onQueue);


  function updateClock() {
    const currentDate = new Date();
    const hours = currentDate.getHours();
    const minutes = currentDate.getMinutes();
    const seconds = currentDate.getSeconds();
    const day = currentDate.getDate();
    const month = currentDate.getMonth();
    const year = currentDate.getFullYear();


    // Formatting numbers two digits
    const formattedHours = hours < 10 ? `0${hours}` : hours;
    const formattedMinutes = minutes < 10 ? `0${minutes}` : minutes;
    const formattedSeconds = seconds < 10 ? `0${seconds}` : seconds;
    const formattedDay = day < 10 ? `0${day}` : day;
    const formattedMonth = monthNames[month];

    const clock = document.getElementById('clock');
    const date = document.getElementById('date');
    clock.textContent = `${formattedHours}:${formattedMinutes}:${formattedSeconds}`;
    date.textContent = `${formattedMonth} ${formattedDay}, ${year}`;
  }

  // Update the clock every second 
  setInterval(updateClock, 1000); // 1000 milliseconds


  return (
    <>
      <div style={{ backgroundColor: '#1F0F53', color: 'white', flexDirection: 'row', display: 'flex', fontSize: '1.8rem' }}>
        <div id="date" style={{ width: '50vw', textAlign: 'right', padding: '.25vh 1.75vw' }}></div>
        <div id="clock" style={{ width: '50vw', padding: '.25vh 1.75vw' }}></div>
      </div>
      <div>
        <Table aria-label="Example table with dynamic content">
          <TableHeader columns={columns}>
            {(column) => <TableColumn key={column.label}>{column.label}</TableColumn>}
          </TableHeader>
          <TableBody items={onGoingReservations}>
            {(item) => (
              <TableRow key={item.name}>
                {(columnKey) => <TableCell>{getKeyValue(item, columnKey)}</TableCell>}
              </TableRow>
            )}
          </TableBody>
        </Table>
      </div>

    </>
  )
}

export default App
