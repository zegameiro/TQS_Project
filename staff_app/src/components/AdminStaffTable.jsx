import { useMutation, useQuery } from "@tanstack/react-query"
import {
  Button,
  Table
} from "flowbite-react"
import { useState } from "react"
import { IoIosAddCircle } from "react-icons/io"
import { deleteEmployee } from "../../actions/deleteActions"
import { getAllEmployees } from "../../actions/getActions"
import axios from "../../api"
import AdminEmployeeModal from "./AdminEmployeeModal"

export default function AdminStaffTable() {
  const [openModal, setOpenModal] = useState(false)
  const [employeeData, setEmployeeData] = useState(null)

  const allEmployees = useQuery({
    queryKey: ["allEmployees"],
    queryFn: () => getAllEmployees(axios),
  })

  const deleteEmployeeMutation = useMutation({
    mutationKey: ["deleteEmployee"],
    mutationFn: (id) => deleteEmployee(axios, id),
    onSuccess: () => allEmployees.refetch(),
  })

  const openCreateEmployeeModal = () => {
    setEmployeeData(null)
    setOpenModal(true)
  }

  console.log(allEmployees?.data)

  return (
    <>
      <div className="flex flex-wrap gap-2 my-5">
        <Button onClick={openCreateEmployeeModal}>
          Add new employee
          <IoIosAddCircle className="ml-2 h-5 w-5" />
        </Button>
      </div>
      <Table hoverable>
        <Table.Head>
          <Table.HeadCell>Name</Table.HeadCell>
          <Table.HeadCell>Email</Table.HeadCell>
          <Table.HeadCell>Phone Number</Table.HeadCell>
          <Table.HeadCell>Specialties</Table.HeadCell>
          {/* <Table.HeadCell>
            <span className="sr-only">Edit</span>
          </Table.HeadCell> */}
          <Table.HeadCell>
            <span className="sr-only">Delete</span>
          </Table.HeadCell>
        </Table.Head>
        <Table.Body className="divide-y">
          {allEmployees?.data?.map((employee) => (
            <Table.Row
              key={employee.id}
              className="bg-white dark:border-gray-700 dark:bg-gray-800"
            >
              <Table.Cell className="whitespace-nowrap font-medium text-gray-900 dark:text-white">
                {employee.fullName}
              </Table.Cell>
              <Table.Cell>{employee.email}</Table.Cell>
              <Table.Cell>{employee.phoneNumber}</Table.Cell>
              <Table.Cell>{employee.specialitiesID.join(", ")}</Table.Cell>
              <Table.Cell>
                {/* <span
                  className="font-medium text-cyan-600 hover:underline dark:text-cyan-500"
                >
                  Edit
                </span> */}
              </Table.Cell>
              <Table.Cell>
                <Button
                  className="btn-sm bg-red-600 hover:bg-red-600"
                  onClick={() => deleteEmployeeMutation.mutate(employee.id)}
                >
                  Delete
                </Button>
              </Table.Cell>
            </Table.Row>
          ))}
        </Table.Body>
      </Table>
      <AdminEmployeeModal
        openModal={openModal}
        setOpenModal={setOpenModal}
        employeeData={employeeData}
        mode={employeeData ? "edit" : "create"}
      />
    </>
  )
}
