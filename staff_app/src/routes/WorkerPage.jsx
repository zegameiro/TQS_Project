import NavbarComponent from "../components/Navbar"

import { Button, Popover, Table } from "flowbite-react"

export default function WorkerPage() {
  return (
    <>
      <div>
        <NavbarComponent />
        <div className="pt-5">
          <Popover
            aria-labelledby="default-popover"
            content={
              <div className="w-64 text-sm text-gray-500 dark:text-gray-400">
                <div className="border-b border-gray-200 bg-gray-100 px-3 py-2 dark:border-gray-600 dark:bg-gray-700">
                  <h3
                    id="default-popover"
                    className="font-semibold text-gray-900 dark:text-white"
                  >
                    Called next customer
                  </h3>
                </div>
                <div className="px-3 py-2">
                  <p>The seat is waiting...</p>
                </div>
              </div>
            }
            arrow={false}
          >
            <Button>Call next customer!</Button>
          </Popover>
        </div>
        <div className="overflow-x-auto pt-5">
          <Table striped>
            <Table.Head>
              <Table.HeadCell>Customer</Table.HeadCell>
              <Table.HeadCell>Service</Table.HeadCell>
              <Table.HeadCell>Category</Table.HeadCell>
              <Table.HeadCell>Timeslot</Table.HeadCell>
            </Table.Head>
            <Table.Body className="divide-y">
              <Table.Row className="bg-white dark:border-gray-700 dark:bg-gray-800">
                <Table.Cell className="whitespace-nowrap font-medium text-gray-900 dark:text-white">
                  Manuela Almeida
                </Table.Cell>
                <Table.Cell>Highlights</Table.Cell>
                <Table.Cell>Hairdressing</Table.Cell>
                <Table.Cell>10h15</Table.Cell>
              </Table.Row>
              <Table.Row className="bg-white dark:border-gray-700 dark:bg-gray-800">
                <Table.Cell className="whitespace-nowrap font-medium text-gray-900 dark:text-white">Miguel Aguiar</Table.Cell>
                <Table.Cell>Buzzcut</Table.Cell>
                <Table.Cell>Hairdressing</Table.Cell>
                <Table.Cell>10h45</Table.Cell>
              </Table.Row>
              <Table.Row className="bg-white dark:border-gray-700 dark:bg-gray-800">
                <Table.Cell className="whitespace-nowrap font-medium text-gray-900 dark:text-white">Nuno Ronaldo</Table.Cell>
                <Table.Cell>Manicure</Table.Cell>
                <Table.Cell>Manicure/Pedicure</Table.Cell>
                <Table.Cell>11h00</Table.Cell>
              </Table.Row>
            </Table.Body>
          </Table>
        </div>
      </div>
    </>
  )
}
