import { useMutation, useQuery } from "@tanstack/react-query"
import { Button, Table } from "flowbite-react"
import { useState } from "react"
import { FaEdit, FaTrashAlt } from "react-icons/fa"
import { IoIosAddCircle, IoMdAdd } from "react-icons/io"
import { deleteFacility } from "../../actions/deleteActions"
import { getAllFacilities } from "../../actions/getActions"
import axios from "../../api"
import AdminFacilityModal from "./AdminFacilityModal"
import AdminRoomModal from "./AdminRoomModal"

export default function AdminFacilitiesTable() {
  const [isOpenFacilityModal, setIsOpenFacilityModal] = useState(false)
  const [facilityData, setFacilityData] = useState(null)
  const [isOpenRoomModal, setIsOpenRoomModal] = useState(false)
  const [selectedFacilityID, setSelectedFacilityID] = useState(null)

  const openEditFacilityModal = (data) => {
    setFacilityData(data)
    setIsOpenFacilityModal(true)
  }

  const openCreateFacilityModal = () => {
    setFacilityData(null)
    setIsOpenFacilityModal(true)
  }

  const openCreateRoomModal = (facilityID) => {
    setSelectedFacilityID(facilityID)
    setIsOpenRoomModal(true)
  }

  const allFacilities = useQuery({
    queryKey: ["allFacilities"],
    queryFn: () => getAllFacilities(axios),
  })

  const deleteFacilityMutation = useMutation({
    mutationKey: ["deleteFacility"],
    mutationFn: (id) => deleteFacility(axios, id),
    onSuccess: () => allFacilities.refetch(),
  })

  return (
    <>
      <div className="flex flex-wrap gap-2 my-5">
        <Button onClick={openCreateFacilityModal}>
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
              <div className="flex flex-wrap gap-2 my-5">
                <Button
                  className="btn-sm"
                  onClick={() => openCreateRoomModal(facility.id)}
                >
                  <IoMdAdd className="h-5 w-5" /> Add Room
                </Button>
                <Button
                  className="btn-sm items-center"
                  onClick={() => openEditFacilityModal(facility)}
                >
                  <FaEdit />
                </Button>
                <Button
                  className="btn-sm bg-red-500 items-center hover:bg-red-600"
                  onClick={() => deleteFacilityMutation.mutate(facility.id)}
                >
                  <FaTrashAlt />
                </Button>
              </div>
            </Table.Row>
          ))}
        </Table.Body>
      </Table>
      <AdminFacilityModal
        openModal={isOpenFacilityModal}
        setOpenModal={setIsOpenFacilityModal}
        facilityData={facilityData}
        mode={facilityData ? "edit" : "create"}
      />
      <AdminRoomModal
        openModal={isOpenRoomModal}
        setOpenModal={setIsOpenRoomModal}
        facilityID={selectedFacilityID}
      />
    </>
  )
}
