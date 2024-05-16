import { useQuery } from "@tanstack/react-query"
import { Button, Table } from "flowbite-react"
import { useState } from "react"
import { IoIosAddCircle } from "react-icons/io"
import { getAllFacilities } from "../../actions/getActions"
import axios from "../../api"
import AdminFacilityModal from "./AdminFacilityModal"

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
          {allFacilities.data?.map((facility) => (
            <Table.Row
              key={facility.id}
              className="bg-white dark:border-gray-700 dark:bg-gray-800"
            >
              <Table.Cell className="whitespace-nowrap font-medium text-gray-900 dark:text-white">
                {facility.id}
              </Table.Cell>
              <Table.Cell>{facility.name}</Table.Cell>
              <Table.Cell>{facility.city}</Table.Cell>
              <Table.Cell>{facility.streetName}</Table.Cell>
              <Table.Cell>{facility.postalCode}</Table.Cell>
              <Table.Cell>{facility.phoneNumber}</Table.Cell>
              <Table.Cell>{facility.rooms ?? "-"}</Table.Cell>
              <Table.Cell>{facility.reservations ?? "-"}</Table.Cell>
              <Table.Cell>
                <span
                  className="font-medium text-cyan-600 hover:underline dark:text-cyan-500"
                  onClick={() =>
                    openEditModal({
                      name: facility.name,
                      city: facility.city,
                      streetName: facility.streetName,
                      postalCode: facility.postalCode,
                      phoneNumber: facility.phoneNumber,
                    })
                  }
                >
                  Edit
                </span>
              </Table.Cell>
            </Table.Row>
          ))}
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
