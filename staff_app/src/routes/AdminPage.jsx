import { Tabs } from "flowbite-react"
import { HiUserCircle } from "react-icons/hi"
import { IoIosBusiness } from "react-icons/io"
import { RiReservedLine } from "react-icons/ri"
import AdminFacilitiesTable from "../components/AdminFacilitiesTable"
import AdminReservationsTable from "../components/AdminReservationsTable"
import AdminStaffTable from "../components/AdminStaffTable"
import NavbarComponent from "../components/Navbar"

export default function AdminPage() {
  return (
    <>
      <div>
        <NavbarComponent />
        <div className="pt-5 mx-10">
          <Tabs aria-label="Tabs with underline" style="underline">
            <Tabs.Item active title="Reservations" icon={RiReservedLine}>
              <AdminReservationsTable />
            </Tabs.Item>
            <Tabs.Item title="People" icon={HiUserCircle}>
              <AdminStaffTable />
            </Tabs.Item>
            <Tabs.Item title="Facilities" icon={IoIosBusiness}>
              <AdminFacilitiesTable />
            </Tabs.Item>
          </Tabs>
        </div>
      </div>
    </>
  )
}
