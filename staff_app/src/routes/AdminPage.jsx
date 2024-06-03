import NavbarComponent from "../components/Navbar"
import { Tabs } from "flowbite-react"
import { HiUserCircle } from "react-icons/hi"
import { IoIosBusiness } from "react-icons/io"
import AdminStaffTable from "../components/AdminStaffTable"
import AdminFacilitiesTable from "../components/AdminFacilitiesTable"

export default function AdminPage() {
  return (
    <>
      <div>
        <NavbarComponent />
        <div className="pt-5 mx-10">
          <Tabs aria-label="Tabs with underline" style="underline">
            <Tabs.Item active title="Staff" icon={HiUserCircle}>
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
