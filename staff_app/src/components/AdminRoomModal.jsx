import { useMutation, useQueryClient } from "@tanstack/react-query"
import { Button, Label, Modal, Select, TextInput } from "flowbite-react"
import PropTypes from "prop-types"
import { useForm } from "react-hook-form"
import { addNewRoom } from "../../actions/postActions"
import { editRoom } from "../../actions/putActions"
import axios from "../../api"
import { beautyServices } from "../utils/beautyServices"

AdminRoomModal.propTypes = {
  openModal: PropTypes.bool.isRequired,
  setOpenModal: PropTypes.func.isRequired,
  facilityID: PropTypes.number.isRequired,
  mode: PropTypes.oneOf(["create", "edit"]).isRequired,
  selectedRoom: PropTypes.object,
}

export default function AdminRoomModal({
  openModal,
  setOpenModal,
  facilityID,
  mode,
  selectedRoom,
}) {
  const queryClient = useQueryClient()

  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm()

  const onCloseModal = () => {
    setOpenModal(false)
    reset()
  }

  const addRoomMutation = useMutation({
    mutationKey: ["addRoom"],
    mutationFn: (roomData) => addNewRoom(axios, roomData),
    onSuccess: () => {
      queryClient.refetchQueries("roomsOfFacility", "allRooms")
    },
  })

  const editRoomMutation = useMutation({
    mutationKey: ["editRoom"],
    mutationFn: (roomData) =>
      console.log(roomData) & editRoom(axios, roomData, selectedRoom.id),
    onSuccess: () => {
      queryClient.refetchQueries("roomsOfFacility", "allRooms")
    },
  })

  const onSubmit = (data) => {
    if (mode === "create") {
      data.facilityID = facilityID
      addRoomMutation.mutate(data)
    } else if (mode === "edit") {
      data.facilityID = 0
      editRoomMutation.mutate(data)
    }
    onCloseModal()
  }

  return (
    <Modal show={openModal} size="lg" onClose={onCloseModal} popup>
      <Modal.Header />
      <Modal.Body>
        <div className="space-y-6">
          <h3 className="text-xl font-medium text-gray-900 dark:text-white">
            {mode === "create" ? "Create" : "Edit"} Room
          </h3>
          <form onSubmit={handleSubmit(onSubmit)}>
            <div>
              <div className="mb-2 block">
                <Label htmlFor="name" value="Name" />
              </div>
              <TextInput
                {...register("name", {
                  required: "This fields is required",
                  maxLength: { value: 50, message: "The name is too long" },
                  minLength: { value: 3, message: "The name is too short" },
                })}
                id="name"
                placeholder="The room's name"
                defaultValue={selectedRoom?.name}
              />
              {errors.name && (
                <span className="text-red-500">{errors.name?.message}</span>
              )}
            </div>
            <div>
              <div className="mb-2 block">
                <Label htmlFor="maxChairsCapacity" value="Max chair capacity" />
              </div>
              <TextInput
                {...register("maxChairsCapacity", {
                  required: "This field is required",
                  pattern: /^\d+$/,
                  valueAsNumber: true,
                })}
                id="maxChairsCapacity"
                placeholder="The maximum chair capacity of the room"
                defaultValue={selectedRoom?.maxChairsCapacity}
              />
              {errors.maxChairsCapacity && (
                <span className="text-red-500">
                  {errors.maxChairsCapacity?.message}
                </span>
              )}
            </div>
            <div>
              <div className="mb-2 block">
                <Label htmlFor="beautyServiceID" value="Beauty service" />
              </div>
              <Select
                {...register("beautyServiceID", {
                  required: "This field is required",
                })}
                id="beautyServiceID"
                defaultValue={selectedRoom?.beautyServiceId.toString()}
                required
              >
                {Object.entries(beautyServices).map(([key, value]) => (
                  <option key={key} value={key}>
                    {value}
                  </option>
                ))}
              </Select>
              {errors.beautyServiceID && (
                <span className="text-red-500">
                  {errors.beautyServiceID?.message}
                </span>
              )}
            </div>
            <div className="flex flex-row mt-4 space-x-5">
              <Button type="submit">
                {mode === "create" ? "Create" : "Edit"} room
              </Button>
              <Button onClick={() => reset()}>Clear</Button>
            </div>
          </form>
        </div>
      </Modal.Body>
    </Modal>
  )
}
