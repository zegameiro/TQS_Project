import { useMutation, useQuery } from "@tanstack/react-query"
import {
  Accordion,
  Button,
  Label,
  Table,
  TableBody,
  TextInput,
} from "flowbite-react"
import PropTypes from "prop-types"
import { useState } from "react"
import { useForm } from "react-hook-form"
import { FaChair, FaEdit, FaTrash, FaTrashAlt } from "react-icons/fa"
import { IoIosAddCircle } from "react-icons/io"
import { deleteChair, deleteRoom } from "../../actions/deleteActions"
import { getAllChairs, getRoomsByFacilityID } from "../../actions/getActions"
import { addNewChair } from "../../actions/postActions"
import axios from "../../api"
import AdminRoomModal from "./AdminRoomModal"
import ChairForm from "./ChairForm"
import { beautyServices } from "../utils/beautyServices"

AdminRoomsTable.propTypes = {
  facilityID: PropTypes.number.isRequired,
}

export default function AdminRoomsTable({ facilityID }) {
  const [isOpenRoomModal, setIsOpenRoomModal] = useState(false)
  const [selectedRoom, setSelectedRoom] = useState(null)

  const { register, handleSubmit, reset } = useForm()

  const roomsOfFacility = useQuery({
    queryKey: ["roomsOfFacility"],
    queryFn: () => getRoomsByFacilityID(axios, facilityID),
  })

  const allChairs = useQuery({
    queryKey: ["allChairs"],
    queryFn: () => getAllChairs(axios),
  })

  const deleteRoomMutation = useMutation({
    mutationKey: ["deleteRoom"],
    mutationFn: (id) => deleteRoom(axios, id),
    onSuccess: () => roomsOfFacility.refetch() && allChairs.refetch(),
  })

  const deleteChairMutation = useMutation({
    mutationKey: ["deleteChair"],
    mutationFn: (id) => deleteChair(axios, id),
    onSuccess: () => allChairs.refetch(),
  })

  const addChairMutation = useMutation({
    mutationKey: ["addChair"],
    mutationFn: (chairData) => addNewChair(axios, chairData),
    onSuccess: () => roomsOfFacility.refetch() && allChairs.refetch(),
  })

  const openCreateRoomModal = () => {
    setSelectedRoom(null)
    setIsOpenRoomModal(true)
  }

  const openEditRoomModal = (roomID) => {
    setSelectedRoom(roomsOfFacility.data.find((room) => room.id === roomID))
    setIsOpenRoomModal(true)
  }

  const onSubmit = (data) => {
    console.log("Add chair to room", data)
    addChairMutation.mutate(data)
    reset()
  }

  const getBeautyService = (serviceId) => {
    return beautyServices[serviceId]
  }

  return (
    <>
      <div className="flex flex-wrap gap-2 my-5">
        <Button onClick={openCreateRoomModal}>
          Create room
          <IoIosAddCircle className="ml-2 h-5 w-5" />
        </Button>
      </div>

      <Accordion collapseAll>
        {roomsOfFacility.data
          ?.sort((a, b) => a.id - b.id)
          .map((room) => {
            const chairsForRoom =
              allChairs?.data?.filter((chair) => chair.room.id === room.id) ||
              []
            return (
              <Accordion.Panel key={room.id}>
                <Accordion.Title>
                  {room.id} - {room.name} - Max. Chairs:{" "}
                  {room.maxChairsCapacity} - Service: {getBeautyService(room.beautyServiceId)}
                </Accordion.Title>
                <Accordion.Content>
                  <h3 className="text-xl font-medium text-gray-900 dark:text-white">
                    Chairs:
                  </h3>
                  <ChairForm roomID={room.id} onSubmit={onSubmit} /> 
                  {chairsForRoom.length === 0 ? (
                    <p>No chairs available</p>
                  ) : (
                    <Table>
                      <Table.Head>
                        <Table.HeadCell>ID</Table.HeadCell>
                        <Table.HeadCell>Name</Table.HeadCell>
                        <Table.HeadCell>Availability</Table.HeadCell>
                        <Table.HeadCell hidden>Actions</Table.HeadCell>
                      </Table.Head>
                      <TableBody>
                        {chairsForRoom.map((chair) => (
                          <Table.Row key={chair.id}>
                            <Table.Cell>{chair.id}</Table.Cell>
                            <Table.Cell>{chair.name}</Table.Cell>
                            <Table.Cell>
                              {chair.isAvailable
                                ? "Available"
                                : "Not Available"}
                            </Table.Cell>
                            <Table.Cell>
                              <Button
                                className="flex btn-sm items-center mt-2 bg-red-500 hover:bg-red-600"
                                onClick={() =>
                                  deleteChairMutation.mutate(chair.id)
                                }
                              >
                                <FaTrash />
                              </Button>
                            </Table.Cell>
                          </Table.Row>
                        ))}
                      </TableBody>
                    </Table>
                  )}
                  <h3 className="pt-3 text-xl font-medium text-gray-900 dark:text-white">
                    Edit room:
                  </h3>
                  <Button
                    className="flex btn-sm items-center"
                    onClick={() => openEditRoomModal(room.id)}
                  >
                    <FaEdit />
                  </Button>
                  <h3 className="pt-3 text-xl font-medium text-gray-900 dark:text-white">
                    Delete room:
                  </h3>
                  <Button
                    className="flex btn-sm bg-red-500 items-center hover:bg-red-600 mt-2"
                    onClick={() => deleteRoomMutation.mutate(room.id)}
                  >
                    <FaTrashAlt />
                  </Button>
                </Accordion.Content>
              </Accordion.Panel>
            )
          })}
      </Accordion>
      <AdminRoomModal
        openModal={isOpenRoomModal}
        setOpenModal={setIsOpenRoomModal}
        facilityID={facilityID}
        mode={selectedRoom ? "edit" : "create"}
        selectedRoom={selectedRoom}
      />
    </>
  )
}
