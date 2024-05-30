import { Table } from "flowbite-react"

export default function AdminReservationsTable() {
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
        <Table.Row className="bg-white dark:border-gray-700 dark:bg-gray-800">
          <Table.Cell className="whitespace-nowrap font-medium text-gray-900 dark:text-white">
            {"Ana Silva"}
          </Table.Cell>
          <Table.Cell>Hairdresser</Table.Cell>
          <Table.Cell>Online</Table.Cell>
          <Table.Cell>
            <span className="font-medium text-cyan-600 hover:underline dark:text-cyan-500">
              Edit
            </span>
          </Table.Cell>
        </Table.Row>
        <Table.Row className="bg-white dark:border-gray-700 dark:bg-gray-800">
          <Table.Cell className="whitespace-nowrap font-medium text-gray-900 dark:text-white">
            Bruno Duarte
          </Table.Cell>
          <Table.Cell>Hairdresser</Table.Cell>
          <Table.Cell>Online</Table.Cell>
          <Table.Cell>
            <a
              href="#"
              className="font-medium text-cyan-600 hover:underline dark:text-cyan-500"
            >
              Edit
            </a>
          </Table.Cell>
        </Table.Row>
        <Table.Row className="bg-white dark:border-gray-700 dark:bg-gray-800">
          <Table.Cell className="whitespace-nowrap font-medium text-gray-900 dark:text-white">
            Carolina Guerreira
          </Table.Cell>
          <Table.Cell>Masseuse</Table.Cell>
          <Table.Cell>Offline</Table.Cell>
          <Table.Cell>
            <a
              href="#"
              className="font-medium text-cyan-600 hover:underline dark:text-cyan-500"
            >
              Edit
            </a>
          </Table.Cell>
        </Table.Row>
      </Table.Body>
    </Table>
  )
}
