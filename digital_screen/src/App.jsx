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

  const reservartions = [
    { name: 'John Doe', onQueue: false, section: 'A', chair: 1 },
    { name: 'Jane Doe', onQueue: false, section: 'B', chair: 4 },
    { name: 'John Smith', onQueue: false, section: 'C', chair: 3 },
    { name: 'Jane Smith', onQueue: false, section: 'D', chair: 2 },
    { name: 'Brian Connor', onQueue: true, section: 'E', chair: null },
    { name: 'Sarah Connor', onQueue: false, section: 'F', chair: 3 },
    { name: 'John Wick', onQueue: false, section: 'G', chair: 14 },
    { name: 'Jane Wick', onQueue: false, section: 'H', chair: 4 },
    { name: 'John Rambo', onQueue: true, section: 'B', chair: null },
    { name: 'Jane Rambo', onQueue: false, section: 'C', chair: 2 },
    { name: 'John McClane', onQueue: false, section: 'D', chair: 3 },
    { name: 'Jane McClane', onQueue: false, section: 'E', chair: 3 },
    { name: 'John Matrix', onQueue: true, section: 'A', chair: null },
    { name: 'Jane Matrix', onQueue: false, section: 'B', chair: 1 },
  ]

  const onGoingReservations = reservartions.filter((reservation) => !reservation.onQueue);


  const [page, setPage] = React.useState(1);
  const rowsPerPage = 8;

  const pages = Math.ceil(onGoingReservations.length / rowsPerPage);

  // const emptyLinesToAdd = rowsPerPage - onGoingReservations.length % rowsPerPage
  // for (let i = 0; i < emptyLinesToAdd; i++) {
  //   onGoingReservations.push({ name: '', onQueue: false, section: '', chair: '' })
  // }

  const items = React.useMemo(() => {
    const start = (page - 1) * rowsPerPage;
    const end = start + rowsPerPage;


    return onGoingReservations.slice(start, end);
  }, [page, onGoingReservations]);

  console.log(onGoingReservations)


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
                    // color="success"
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
                  <TableRow key={item.name}>
                    {(columnKey) => <TableCell>{getKeyValue(item, columnKey)}</TableCell>}
                  </TableRow>
                )}
              </TableBody>
            </Table>
          </div>
          <div style={{ width: '5vw' }}>
          </div>
          <div style={{ width: '35vw', padding: '2vh' }}>
            <h2>Queue</h2>
            <ul>
              {reservartions.map((reservation) => {
                if (reservation.onQueue) {
                  return <li key={reservation.name}>{reservation.name}</li>
                }
              })}
            </ul>
          </div>
        </div>
      </div>

    </>
  )
}

export default App
