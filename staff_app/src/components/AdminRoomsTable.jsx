import { useMutation, useQuery } from "@tanstack/react-query"
import { Accordion, Button, Table, TableBody } from "flowbite-react"
import PropTypes from "prop-types"
import { useState } from "react"
import { FaEdit, FaTrashAlt } from "react-icons/fa"
import { IoIosAddCircle } from "react-icons/io"
import { deleteRoom } from "../../actions/deleteActions"
import { getRoomsByFacilityID } from "../../actions/getActions"
import axios from "../../api"
import AdminRoomModal from "./AdminRoomModal"

AdminRoomsTable.propTypes = {
  facilityID: PropTypes.number.isRequired,
}

export default function AdminRoomsTable({ facilityID }) {
  const [isOpenRoomModal, setIsOpenRoomModal] = useState(false)
  const [selectedRoom, setSelectedRoom] = useState(null)

  const roomsOfFacility = useQuery({
    queryKey: ["roomsOfFacility"],
    queryFn: () => getRoomsByFacilityID(axios, facilityID),
  })

  const deleteRoomMutation = useMutation({
    mutationKey: ["deleteRoom"],
    mutationFn: (id) => deleteRoom(axios, id),
    onSuccess: () => roomsOfFacility.refetch(),
  })

  const openCreateRoomModal = () => {
    setSelectedRoom(null)
    setIsOpenRoomModal(true)
  }

  const openEditRoomModal = (roomID) => {
    setSelectedRoom(roomsOfFacility.data.find((room) => room.id === roomID))
    setIsOpenRoomModal(true)
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
        {roomsOfFacility.data?.map((room) => (
          <Accordion.Panel key={room.id}>
            <Accordion.Title>
              {room.id} - {room.name} - Max. Chairs: {room.maxChairsCapacity}
            </Accordion.Title>
            <Accordion.Content>
              <h3 className="text-xl font-medium text-gray-900 dark:text-white">
                Chairs:
              </h3>
              {(room.chairs?.length || 0) === 0 ? (
                <p>No chairs available</p>
              ) : (
                <Table>
                  <Table.Head>
                    <Table.HeadCell>ID</Table.HeadCell>
                    <Table.HeadCell>Name</Table.HeadCell>
                    <Table.HeadCell>Availability</Table.HeadCell>
                  </Table.Head>
                  <TableBody>
                    {room.chairs.map((chair) => (
                      <Table.Row key={chair.id}>
                        <Table.Cell>{chair.id}</Table.Cell>
                        <Table.Cell>{chair.name}</Table.Cell>
                        <Table.Cell>
                          {chair.isAvailable ? "Available" : "Not Available"}
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
        ))}
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
