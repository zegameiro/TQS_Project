import { useQuery } from "@tanstack/react-query"
import {
  Button,
  Checkbox,
  Label,
  Modal,
  Table,
  TextInput,
} from "flowbite-react"
import { useState } from "react"
import { getAllEmployees } from "../../actions/getActions"
import axios from "../../api"

export default function AdminStaffTable() {
  const [openModal, setOpenModal] = useState(false)
  const [firstName, setFirstName] = useState("")
  const [email, setEmail] = useState("")

  const allEmployees = useQuery({
    queryKey: ["allEmployees"],
    queryFn: () => getAllEmployees(axios),
  })

  function onCloseModal() {
    setOpenModal(false)
    setEmail("")
  }

  return (
    <Table hoverable>
      <Table.Head>
        <Table.HeadCell>Name</Table.HeadCell>
        <Table.HeadCell>Email</Table.HeadCell>
        <Table.HeadCell>Phone Number</Table.HeadCell>
        <Table.HeadCell>Specialties</Table.HeadCell>
        <Table.HeadCell>
          <span className="sr-only">Edit</span>
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
            <Table.Cell>{employee.specialtiesId}</Table.Cell>
            <Table.Cell>
              <span
                className="font-medium text-cyan-600 hover:underline dark:text-cyan-500"
                onClick={() => setOpenModal(true)}
              >
                Edit
              </span>
              <Modal show={openModal} size="lg" onClose={onCloseModal} popup>
                <Modal.Header />
                <Modal.Body>
                  <div className="space-y-6">
                    <h3 className="text-xl font-medium text-gray-900 dark:text-white">
                      Edit user
                    </h3>
                    <div>
                      <div className="mb-2 block">
                        <Label htmlFor="firstName" value="First Name" />
                      </div>
                      <TextInput
                        id="firstName"
                        placeholder="Your first name"
                        value={firstName}
                        onChange={(event) => setFirstName(event.target.value)}
                        required
                      />
                    </div>
                    <div>
                      <div className="mb-2 block">
                        <Label htmlFor="email" value="Your email" />
                      </div>
                      <TextInput
                        id="email"
                        placeholder="name@company.com"
                        value={email}
                        onChange={(event) => setEmail(event.target.value)}
                        required
                      />
                    </div>
                    <div>
                      <div className="mb-2 block">
                        <Label htmlFor="email" value="Your email" />
                      </div>
                      <TextInput
                        id="email"
                        placeholder="name@company.com"
                        value={email}
                        onChange={(event) => setEmail(event.target.value)}
                        required
                      />
                    </div>
                    <div>
                      <div className="mb-2 block">
                        <Label htmlFor="password" value="Your password" />
                      </div>
                      <TextInput id="password" type="password" required />
                    </div>
                    <div className="flex justify-between">
                      <div className="flex items-center gap-2">
                        <Checkbox id="remember" />
                        <Label htmlFor="remember">Remember me</Label>
                      </div>
                      <a
                        href="#"
                        className="text-sm text-cyan-700 hover:underline dark:text-cyan-500"
                      >
                        Lost Password?
                      </a>
                    </div>
                    <div className="w-full">
                      <Button>Log in to your account</Button>
                    </div>
                    <div className="flex justify-between text-sm font-medium text-gray-500 dark:text-gray-300">
                      Not registered?&nbsp;
                      <a
                        href="#"
                        className="text-cyan-700 hover:underline dark:text-cyan-500"
                      >
                        Create account
                      </a>
                    </div>
                  </div>
                </Modal.Body>
              </Modal>
            </Table.Cell>
          </Table.Row>
        ))}
      </Table.Body>
    </Table>
  )
}
