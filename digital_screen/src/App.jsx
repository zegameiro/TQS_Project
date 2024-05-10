import React from "react";
import './App.css'

import sound from './assets/call.mp3'

import { Table, TableHeader, TableColumn, TableBody, TableRow, TableCell, Pagination, getKeyValue } from "@nextui-org/react";

function App() {

  function playSound() {
    const audio = new Audio(sound)
    audio.play()
  }

  const monthNames = [
    'January', 'February', 'March', 'April', 'May', 'June',
    'July', 'August', 'September', 'October', 'November', 'December'
  ];


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

  const columns = [
    {
      label: "section",
      width: "15%",
    },
    {
      label: "chair",
      width: "15%",
    },
    {
      label: "name",
      width: "70%",
    },
  ];


  const reservations = [
    { id: 1, name: 'John Doe', onQueue: false, section: 'A', chair: 1 },
    { id: 2, name: 'Jane Doe', onQueue: false, section: 'B', chair: 4 },
    { id: 3, name: 'John Smith', onQueue: false, section: 'C', chair: 3 },
    { id: 4, name: 'Jane Smith', onQueue: false, section: 'D', chair: 2 },
    { id: 5, name: 'Brian Connor', onQueue: true, section: 'E', chair: null },
    { id: 6, name: 'Sarah Connor', onQueue: false, section: 'F', chair: 3 },
    { id: 7, name: 'John Wick', onQueue: false, section: 'G', chair: 14 },
    { id: 8, name: 'Jane Wick', onQueue: false, section: 'H', chair: 4 },
    { id: 9, name: 'John Rambo', onQueue: true, section: 'B', chair: null },
    { id: 10, name: 'Jane Rambo', onQueue: false, section: 'C', chair: 2 },
    { id: 11, name: 'John McClane', onQueue: false, section: 'D', chair: 3 },
    { id: 12, name: 'Jane McClane', onQueue: false, section: 'E', chair: 3 },
    { id: 13, name: 'John Matrix', onQueue: true, section: 'A', chair: null },
    { id: 14, name: 'Jane Matrix', onQueue: false, section: 'B', chair: 1 },
    { id: 15, name: 'Tony Stark', onQueue: true, section: 'B', chair: null },
    { id: 16, name: 'Dwayne Johnson', onQueue: true, section: 'C', chair: null },
    { id: 17, name: 'John Cena', onQueue: true, section: 'D', chair: null },
    { id: 18, name: 'Cristiano Ronaldo', onQueue: true, section: 'E', chair: null },
    { id: 19, name: 'Lionel Messi', onQueue: true, section: 'F', chair: null },
    { id: 20, name: 'Elon Musk', onQueue: true, section: 'G', chair: null },
    { id: 22, name: 'Bill Gates', onQueue: true, section: 'A', chair: null },
    { id: 24, name: 'Mark Zuckerberg', onQueue: true, section: 'C', chair: null },
    { id: 25, name: 'Joe Biden', onQueue: true, section: 'D', chair: null },
    { id: 26, name: 'Donald Trump', onQueue: true, section: 'E', chair: null },
    { id: 27, name: 'Barack Obama', onQueue: true, section: 'F', chair: null },
    { id: 28, name: 'Emmanuel Macron', onQueue: true, section: 'G', chair: null },
  ]

  const onGoingReservations = reservations.filter((reservation) => !reservation.onQueue);
  const onQueueReservations = reservations.filter((reservation) => reservation.onQueue);

  {
    onQueueReservations.map((reservation) => {
      reservation.hours = Math.floor(Math.random() * 8) + 8
      reservation.minutes = Math.floor(Math.random() * 4) * 15
    })
  }

  onQueueReservations.sort((a, b) => {
    if (a.hours < b.hours) {
      return -1
    }
    if (a.hours > b.hours) {
      return 1
    }
    if (a.minutes < b.minutes) {
      return -1
    }
    if (a.minutes > b.minutes) {
      return 1
    }
    return 0
  })


  const [page, setPage] = React.useState(1);
  const rowsPerPage = 8;

  const pages = Math.ceil(onGoingReservations.length / rowsPerPage);

  const emptyLinesToAdd = rowsPerPage - onGoingReservations.length % rowsPerPage
  for (let i = 0; i < emptyLinesToAdd; i++) {
    onGoingReservations.push({ id: "blank" + i, name: '', onQueue: false, section: '', chair: '' })
  }

  const items = React.useMemo(() => {
    const start = (page - 1) * rowsPerPage;
    const end = start + rowsPerPage;


    return onGoingReservations.slice(start, end);
  }, [page, onGoingReservations]);


  React.useEffect(() => {
    const changePage = setInterval(() => {
      if (page === pages) {
        setPage(1);
      } else {
        setPage(page + 1);
      }
    }, 20000);

    return () => clearInterval(changePage);
  }, [page, pages]);


  return (
    <>
      <div style={{ backgroundColor: '#1F0F53', color: 'white', flexDirection: 'row', display: 'flex', fontSize: '5vh' }}>
        <div id="date" style={{ width: '50vw', textAlign: 'right', padding: '.25vh 1.75vw' }}></div>
        <div id="clock" style={{ width: '50vw', padding: '.25vh 1.75vw' }}></div>
      </div>
      <div style={{ margin: '1.5vw' }}>
        <div style={{ display: 'flex', flexDirection: 'row', justifyContent: 'space-between' }}>
          <div style={{ width: '60vw', padding: '2vh' }}>
            <Table isStriped removeWrapper aria-label="Example table with dynamic content"
              bottomContent={
                <div className="flex w-full justify-center">
                  <Pagination
                    isCompact
                    showControls
                    showShadow
                    color="primary"
                    className="text-white"
                    page={page}
                    total={pages}
                    onChange={(page) => setPage(page)}
                  />
                </div>
              }
            >
              <TableHeader columns={columns}>
                {(column) => <TableColumn key={column.label} width={column.width} >{column.label}</TableColumn>}
              </TableHeader>
              <TableBody emptyContent={"No clients being attended"} items={items}>
                {(item) => (
                  <TableRow key={item.id}>
                    {(columnKey) => <TableCell>{getKeyValue(item, columnKey)}</TableCell>}
                  </TableRow>
                )}
              </TableBody>
            </Table>
          </div>
          <div style={{ width: '2vw' }}>
          </div>
          <div style={{ width: '38vw', border: '.125rem solid #220f67', borderRadius: '25px', padding: '2vh' }}>
            <h1 className="text-primary" style={{ fontSize: '2rem', marginBottom: '3vh', fontWeight:'bold' }}>On queue</h1>
            <div style={{ display: 'flex', justifyContent: 'space-between' }}>
              <div style={{ width: '50%' }}>
                {onQueueReservations.slice(0, 13).map((reservation) => (
                  <div key={reservation.id} style={{ height: '5.5vh', fontSize: '2.5vh' }}>
                    {String(reservation.hours).padStart(2, '0')}:{String(reservation.minutes).padStart(2, '0')} <span style={{ fontWeight: 'bold', marginLeft: '1rem' }}>{reservation.name}</span>
                  </div>
                ))}
              </div>
              <div style={{ width: '50%' }}>
                {onQueueReservations.slice(13).map((reservation) => (
                  <div key={reservation.id} style={{ height: '5.5vh', fontSize: '2.5vh' }}>
                    {String(reservation.hours).padStart(2, '0')}:{String(reservation.minutes).padStart(2, '0')} <span style={{ fontWeight: 'bold', marginLeft: '1rem' }}>{reservation.name}</span>
                  </div>
                ))}
              </div>
            </div>
          </div>
        </div>
      </div>

    </>
  )
}

export default App
