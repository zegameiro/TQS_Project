import { useQuery } from "@tanstack/react-query"
import { getFacilityById } from "../../actions/getActions"
import axios from "../../api"
import NavbarComponent from "../components/Navbar"
import { useParams } from "react-router-dom"
import AdminRoomsTable from "../components/AdminRoomsTable"

export default function FacilityPage() {
  const facilityID = useParams()?.id

  const facility = useQuery({
    queryKey: ["selectedFacility"],
    queryFn: () => getFacilityById(axios, facilityID),
    enabled: !!facilityID,
  })

  return (
    <>
      <div>
        <NavbarComponent />
        <div className="pt-5 mx-10">
          <div className="pt-5 mx-10">
            <h1 className="text-3xl font-semibold">Facility Details</h1>
            <div className="flex flex-wrap gap-2 my-5">
              <div className="flex flex-col gap-2">
                <span className="text-lg font-semibold">
                  ID: {facility.data?.id}
                </span>
                <span className="text-lg font-semibold">
                  Name: {facility.data?.name}
                </span>
                <span className="text-lg font-semibold">
                  City: {facility.data?.city}
                </span>
                <span className="text-lg font-semibold">
                  Street Name: {facility.data?.streetName}
                </span>
                <span className="text-lg font-semibold">
                  Postal Code: {facility.data?.postalCode}
                </span>
                <span className="text-lg font-semibold">
                  Phone Number: {facility.data?.phoneNumber}
                </span>
              </div>
            </div>
          </div>
          <AdminRoomsTable facilityID={parseInt(facilityID)} />
        </div>
      </div>
    </>
  )
}
