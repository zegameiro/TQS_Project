import { Button, Table } from "flowbite-react"
import { useState } from "react"
import { IoIosAddCircle } from "react-icons/io"
import AdminFacilityModal from "./AdminFacilityModal"

import { useQuery } from "@tanstack/react-query"

import { getTest, getAllFacilities } from "../../actions/getActions"

import axios from "../../api"

export default function AdminFacilitiesTable() {
  const [isOpenModal, setIsOpenModal] = useState(false)
  const [facilityData, setFacilityData] = useState(null)

  const openEditModal = (data) => {
    setFacilityData(data)
    setIsOpenModal(true)
  }

  const openCreateModal = () => {
    setFacilityData(null)
    setIsOpenModal(true)
  }

  const testConnection = useQuery({
    queryKey: ["test"],
    queryFn: () => getTest(axios),
  })

  const allFacilities = useQuery({
    queryKey: ["allFacilities"],
    queryFn: () => getAllFacilities(axios),
  })

  return (
    <>
      <div className="flex flex-wrap gap-2 my-5">
        <Button onClick={openCreateModal}>
          Create facility
          <IoIosAddCircle className="ml-2 h-5 w-5" />
        </Button>
      </div>
      <Table hoverable>
        <Table.Head>
          <Table.HeadCell>ID</Table.HeadCell>
          <Table.HeadCell>Name</Table.HeadCell>
          <Table.HeadCell>City</Table.HeadCell>
          <Table.HeadCell>Street Name</Table.HeadCell>
          <Table.HeadCell>Postal Code</Table.HeadCell>
          <Table.HeadCell>Phone Number</Table.HeadCell>
          <Table.HeadCell>Rooms</Table.HeadCell>
          <Table.HeadCell>Reservations</Table.HeadCell>
          <Table.HeadCell>
            <span className="sr-only">Edit</span>
          </Table.HeadCell>
        </Table.Head>
        <Table.Body className="divide-y">
          <Table.Row className="bg-white dark:border-gray-700 dark:bg-gray-800">
            <Table.Cell className="whitespace-nowrap font-medium text-gray-900 dark:text-white">
              1
            </Table.Cell>
            <Table.Cell>BeautyPlaza Aveiro</Table.Cell>
            <Table.Cell>Aveiro</Table.Cell>
            <Table.Cell>Avenida Dr. Lourenço Peixinho</Table.Cell>
            <Table.Cell>3800-160</Table.Cell>
            <Table.Cell>234123456</Table.Cell>
            <Table.Cell>TODO rooms</Table.Cell>
            <Table.Cell>TODO reservations</Table.Cell>
            <Table.Cell>
              <span
                className="font-medium text-cyan-600 hover:underline dark:text-cyan-500"
                onClick={() =>
                  // TODO: redo this to not repeat the data
                  openEditModal({
                    name: "BeautyPlaza Aveiro",
                    city: "Aveiro",
                    streetName: "Avenida Dr. Lourenço Peixinho",
                    postalCode: "3800-160",
                    phoneNumber: "234123456",
                  })
                }
              >
                Edit
              </span>
            </Table.Cell>
          </Table.Row>
        </Table.Body>
      </Table>
      <AdminFacilityModal
        openModal={isOpenModal}
        setOpenModal={setIsOpenModal}
        facilityData={facilityData}
        mode={facilityData ? "edit" : "create"}
      />
    </>
  )
}
