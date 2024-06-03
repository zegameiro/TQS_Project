import { useMutation, useQueryClient } from "@tanstack/react-query"
import {
  Button,
  Checkbox,
  Label,
  Modal,
  TextInput,
  ToggleSwitch,
} from "flowbite-react"
import PropTypes from "prop-types"
import { useEffect } from "react"
import { useForm } from "react-hook-form"
import { addNewEmployee } from "../../actions/postActions"
import axios from "../../api"
import { useState } from "react"
import { beautyServices } from "../utils/beautyServices"

AdminEmployeeModal.propTypes = {
  openModal: PropTypes.bool.isRequired,
  setOpenModal: PropTypes.func.isRequired,
  employeeData: PropTypes.object,
  mode: PropTypes.oneOf(["create", "edit"]).isRequired,
}

export default function AdminEmployeeModal({
  openModal,
  setOpenModal,
  employeeData,
  mode,
}) {
const beautyServicesArray = Object.keys(beautyServices).map((key) => ({
    id: parseInt(key),
    name: beautyServices[key],
}))
  const [switches, setSwitches] = useState(beautyServicesArray.map(() => false))

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

  const addEmployeeMutation = useMutation({
    mutationKey: ["addEmployee"],
    mutationFn: (employeeData) => addNewEmployee(axios, employeeData),
    onSuccess: () => {
      queryClient.refetchQueries("allFacilities")
    },
  })

  //   const editEmployeeMutation = useMutation({
  //     mutationKey: ["editEmployee"],
  //     mutationFn: (employeeData) =>
  //       editEmployee(axios, employeeData, employeeData.id),
  //     onSuccess: () => {
  //       queryClient.refetchQueries("allFacilities")
  //     },
  //   })

  const onSubmit = (data) => {
    const selectedSpecialities = beautyServicesArray
      .filter((_, index) => switches[index])
      .map((service) => service.id)
    data.specialitiesID = selectedSpecialities
    if (mode === "create") {
      addEmployeeMutation.mutate(data)
      // } else if (mode === "edit") {
      //   editEmployeeMutation.mutate(data)
    }
    onCloseModal()
  }

  const handleToggleChange = (index) => {
    setSwitches((prev) => {
      const newSwitches = [...prev]
      newSwitches[index] = !newSwitches[index]
      return newSwitches
    })
  }

  useEffect(() => {
    if (mode === "edit" && employeeData) {
      Object.keys(employeeData).forEach((key) => {
        setValue(key, employeeData[key])
      })
      setSwitches(
        beautyServicesArray.map((service) =>
          employeeData.specialitiesID.includes(service.id)
        )
      )
    } else {
      reset()
      setSwitches(beautyServicesArray.map(() => false))
    }
  }, [employeeData, mode, setValue, reset])

  return (
    <Modal show={openModal} size="lg" onClose={onCloseModal} popup>
      <Modal.Header />
      <Modal.Body>
        <div className="space-y-6">
          <h3 className="text-xl font-medium text-gray-900 dark:text-white">
            {mode === "edit" ? "Edit Employee" : "Create Employee"}
          </h3>
          <form onSubmit={handleSubmit(onSubmit)}>
            <div>
              <div className="mb-2 block">
                <Label htmlFor="fullName" value="Full Name" />
              </div>
              <TextInput
                {...register("fullName", {
                  required: "This fields is required",
                  maxLength: { value: 200, message: "The name is too long" },
                  minLength: { value: 5, message: "The name is too short" },
                })}
                id="fullName"
                placeholder="The employee's name"
              />
              {errors.name && (
                <span className="text-red-500">{errors.name?.message}</span>
              )}
            </div>
            <div>
              <div className="mb-2 block">
                <Label htmlFor="email" value="Email" />
              </div>
              <TextInput
                {...register("email", {
                  required: "This field is required",
                  maxLength: { value: 40, message: "The email is too long" },
                  minLength: { value: 5, message: "The email is too short" },
                })}
                id="email"
                placeholder="The employee's email"
              />
              {errors.city && (
                <span className="text-red-500">{errors.city?.message}</span>
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
                placeholder="The phone number of the employee"
              />
              {errors.phoneNumber && (
                <span className="text-red-500">
                  {errors.phoneNumber?.message}
                </span>
              )}
            </div>
            <div>
              <div className="mb-2 block">
                <Label htmlFor="isAdmin" value="Is the employee admin? " />
              </div>
              <Checkbox id="isAdmin" {...register("isAdmin")} />
            </div>
            <div>
              <div className="mb-2 block">
                <Label htmlFor="specialities" value="Specialities" />
              </div>
              <div className="flex max-w-md flex-col gap-4">
                {beautyServicesArray.map((service, index) => (
                  <ToggleSwitch
                    key={service.id}
                    checked={switches[index]}
                    label={service.name}
                    onChange={() => handleToggleChange(index)}
                  />
                ))}
              </div>
            </div>
            <div className="flex flex-row mt-4 space-x-5">
              <Button type="submit">
                {mode === "edit" ? "Save Changes" : "Create Employee"}
              </Button>
              <Button onClick={() => reset()}>Clear</Button>
            </div>
          </form>
        </div>
      </Modal.Body>
    </Modal>
  )
}
