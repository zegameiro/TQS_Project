import { useQuery } from "@tanstack/react-query"
import { Table } from "flowbite-react"
import { getAllReservations } from "../../actions/getActions"
import axios from "../../api"

export default function AdminReservationsTable() {
  const allReservations = useQuery({
    queryKey: ["allReservations"],
    queryFn: () => getAllReservations(axios),
  })

  const convertTimestampIntoUsableDate = (timestamp) => {
    const dateStrings = (new Date(timestamp)).toISOString().split("T")
    return `${dateStrings[0]} - ${dateStrings[1].split(".")[0]}`
  }

  console.log(allReservations.data)

  return (
    <Table hoverable>
      <Table.Head>
        <Table.HeadCell>Date and Hour</Table.HeadCell>
        <Table.HeadCell>Customer Name</Table.HeadCell>
        <Table.HeadCell>Customer Email</Table.HeadCell>
        <Table.HeadCell>Customer Phone Number</Table.HeadCell>
        <Table.HeadCell>Employee</Table.HeadCell>
        <Table.HeadCell>Secret Code</Table.HeadCell>
      </Table.Head>
      <Table.Body className="divide-y">
        {allReservations.data?.map((reservation) => (
          <Table.Row
            key={reservation.id}
            className="bg-white dark:border-gray-700 dark:bg-gray-800"
          >
            <Table.Cell>
              {convertTimestampIntoUsableDate(reservation.timestamp)}
            </Table.Cell>
            <Table.Cell>{reservation.customerName}</Table.Cell>
            <Table.Cell>{reservation.customerEmail}</Table.Cell>
            <Table.Cell>{reservation.customerPhoneNumber}</Table.Cell>
            <Table.Cell>{reservation.employee.fullName}</Table.Cell>
            <Table.Cell className="whitespace-nowrap font-medium text-red-500 dark:text-white">{reservation.secretCode}</Table.Cell> 
          </Table.Row>
        ))}
      </Table.Body>
    </Table>
  )
}
