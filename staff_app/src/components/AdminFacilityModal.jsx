import { useMutation, useQueryClient } from "@tanstack/react-query"
import { Button, Label, Modal, TextInput } from "flowbite-react"
import PropTypes from "prop-types"
import { useEffect } from "react"
import { useForm } from "react-hook-form"
import { addNewFacility } from "../../actions/postActions"
import { editFacility } from "../../actions/putActions"
import axios from "../../api"

AdminFacilityModal.propTypes = {
  openModal: PropTypes.bool.isRequired,
  setOpenModal: PropTypes.func.isRequired,
  facilityData: PropTypes.object,
  mode: PropTypes.oneOf(["create", "edit"]).isRequired,
}

export default function AdminFacilityModal({
  openModal,
  setOpenModal,
  facilityData,
  mode,
}) {
  const queryClient = useQueryClient()

  const {
    register,
    handleSubmit,
    reset,
    setValue,
    formState: { errors },
  } = useForm()

  const onCloseModal = () => {
    setOpenModal(false)
    reset()
  }

  const addFacilityMutation = useMutation({
    mutationKey: ["addFacility"],
    mutationFn: (facilityData) => addNewFacility(axios, facilityData),
    onSuccess: () => {
      queryClient.refetchQueries("allFacilities")
    },
  })

  const editFacilityMutation = useMutation({
    mutationKey: ["editFacility"],
    mutationFn: (facilityData) => editFacility(axios, facilityData),
    onSuccess: () => {
      queryClient.refetchQueries("allFacilities")
    },
  })

  const onSubmit = (data) => {
    if (mode === "create") {
      addFacilityMutation.mutate(data)
    } else if (mode === "edit") {
      editFacilityMutation.mutate(data)
    }
    onCloseModal()
  }

  useEffect(() => {
    if (mode === "edit" && facilityData) {
      Object.keys(facilityData).forEach((key) => {
        setValue(key, facilityData[key])
      })
    } else {
      reset()
    }
  }, [facilityData, mode, setValue, reset])

  return (
    <Modal show={openModal} size="lg" onClose={onCloseModal} popup>
      <Modal.Header />
      <Modal.Body>
        <div className="space-y-6">
          <h3 className="text-xl font-medium text-gray-900 dark:text-white">
            {mode === "edit" ? "Edit Facility" : "Create Facility"}
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
                  minLength: { value: 5, message: "The name is too short" },
                })}
                id="name"
                placeholder="The facility's name"
              />
              {errors.name && (
                <span className="text-red-500">{errors.name?.message}</span>
              )}
            </div>
            <div>
              <div className="mb-2 block">
                <Label htmlFor="city" value="City" />
              </div>
              <TextInput
                {...register("city", {
                  required: "This field is required",
                  maxLength: { value: 40, message: "The city is too long" },
                  minLength: { value: 5, message: "The city is too short" },
                })}
                id="city"
                placeholder="The facility's city"
              />
              {errors.city && (
                <span className="text-red-500">{errors.city?.message}</span>
              )}
            </div>
            <div>
              <div className="mb-2 block">
                <Label htmlFor="streetName" value="Street Name" />
              </div>
              <TextInput
                {...register("streetName", {
                  required: "This field is required",
                  maxLength: {
                    value: 70,
                    message: "The street name is too long",
                  },
                  minLength: {
                    value: 5,
                    message: "The street name is too short",
                  },
                })}
                id="streetName"
                placeholder="The street name where the facility is located"
              />
              {errors.streetName && (
                <span className="text-red-500">
                  {errors.streetName?.message}
                </span>
              )}
            </div>
            <div>
              <div className="mb-2 block">
                <Label htmlFor="postalCode" value="Postal Code" />
              </div>
              <TextInput
                {...register("postalCode", {
                  required: "This field is required",
                  maxLength: {
                    value: 8,
                    message: "The postal code is too long",
                  },
                  minLength: {
                    value: 8,
                    message: "The postal code is too short",
                  },
                })}
                id="postalCode"
                placeholder="The postal code of the facility"
              />
              {errors.postalCode && (
                <span className="text-red-500">
                  {errors.postalCode?.message}
                </span>
              )}
            </div>
            <div>
              <div className="mb-2 block">
                <Label htmlFor="phoneNumber" value="Phone Number" />
              </div>
              <TextInput
                {...register("phoneNumber", {
                  required: "This field is required",
                  pattern: {
                    value: /^\d{9}$/,
                    message: "The phone number must have 9 digits",
                  },
                  maxLength: {
                    value: 9,
                    message: "The phone number is too long",
                  },
                })}
                id="phoneNumber"
                placeholder="The phone number of the facility"
              />
              {errors.phoneNumber && (
                <span className="text-red-500">
                  {errors.phoneNumber?.message}
                </span>
              )}
            </div>

            <div className="flex flex-row mt-4 space-x-5">
              <Button type="submit">
                {mode === "edit" ? "Save Changes" : "Create Facility"}
              </Button>
              <Button onClick={() => reset()}>Clear</Button>
            </div>
          </form>
        </div>
      </Modal.Body>
    </Modal>
  )
}
