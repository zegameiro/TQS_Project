import { useQuery } from "@tanstack/react-query"
import { Table } from "flowbite-react"
import { getAllReservations } from "../../actions/getActions"
import axios from "../../api"

export default function AdminReservationsTable() {
  const allReservations = useQuery({
    queryKey: ["allReservations"],
    queryFn: () => getAllReservations(axios),
  })

  console.log(allReservations.data)

  return (
    <Table hoverable>
      <Table.Head>
        <Table.HeadCell>Timestamp</Table.HeadCell>
        <Table.HeadCell>Customer Name</Table.HeadCell>
        <Table.HeadCell>Customer Email</Table.HeadCell>
        <Table.HeadCell>Customer Phone Number</Table.HeadCell>
        <Table.HeadCell>Specialty</Table.HeadCell>
        <Table.HeadCell>Room</Table.HeadCell>
      </Table.Head>
      <Table.Body className="divide-y">
        {allReservations.data?.map((reservation) => (
          <Table.Row
            key={reservation.id}
            className="bg-white dark:border-gray-700 dark:bg-gray-800"
          >
            <Table.Cell className="whitespace-nowrap font-medium text-gray-900 dark:text-white">
              {reservation.timestamp}
            </Table.Cell>
            <Table.Cell>{reservation.customerName}</Table.Cell>
            <Table.Cell>{reservation.customerEmail}</Table.Cell>
            <Table.Cell>{reservation.customerPhone}</Table.Cell>
            <Table.Cell>{reservation.specialtyId}</Table.Cell>
            <Table.Cell>{reservation.roomId}</Table.Cell>
          </Table.Row>
        ))}
      </Table.Body>
    </Table>
  )
}
