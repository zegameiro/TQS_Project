import { Button, Label, Modal, TextInput } from "flowbite-react"
import { useEffect, useState } from "react"
import PropTypes from "prop-types"

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
  const [name, setName] = useState("")
  const [city, setCity] = useState("")
  const [streetName, setStreetName] = useState("")
  const [postalCode, setPostalCode] = useState("")
  const [phoneNumber, setPhoneNumber] = useState("")

  useEffect(() => {
    if (mode === "edit" && facilityData) {
      setName(facilityData.name || "")
      setCity(facilityData.city || "")
      setStreetName(facilityData.streetName || "")
      setPostalCode(facilityData.postalCode || "")
      setPhoneNumber(facilityData.phoneNumber || "")
    } else {
      setName("")
      setCity("")
      setStreetName("")
      setPostalCode("")
      setPhoneNumber("")
    }
  }, [openModal, mode, facilityData])

  function onCloseModal() {
    setOpenModal(false)
  }

  function handleSubmit() {
    // TODO: Handle form submission (e.g., API call to save/update facility)
    console.log("Form submitted")
    onCloseModal()
  }

  return (
    <Modal show={openModal} size="lg" onClose={onCloseModal} popup>
      <Modal.Header />
      <Modal.Body>
        <div className="space-y-6">
          <h3 className="text-xl font-medium text-gray-900 dark:text-white">
            {mode === "edit" ? "Edit Facility" : "Create Facility"}
          </h3>
          <div>
            <div className="mb-2 block">
              <Label htmlFor="name" value="Name" />
            </div>
            <TextInput
              id="name"
              placeholder="The facilities name"
              value={name}
              onChange={(event) => setName(event.target.value)}
              required
            />
          </div>
          <div>
            <div className="mb-2 block">
              <Label htmlFor="city" value="City" />
            </div>
            <TextInput
              id="city"
              placeholder="The facilities city"
              value={city}
              onChange={(event) => setCity(event.target.value)}
              required
            />
          </div>
          <div>
            <div className="mb-2 block">
              <Label htmlFor="streetName" value="Street Name" />
            </div>
            <TextInput
              id="streetName"
              placeholder="The street name where the facility is located"
              value={streetName}
              onChange={(event) => setStreetName(event.target.value)}
              required
            />
          </div>
          <div>
            <div className="mb-2 block">
              <Label htmlFor="postalCode" value="Postal Code" />
            </div>
            <TextInput
              id="postalCode"
              placeholder="The postal code of the facility"
              value={postalCode}
              onChange={(event) => setPostalCode(event.target.value)}
              required
            />
          </div>
          <div>
            <div className="mb-2 block">
              <Label htmlFor="phoneNumber" value="Phone Number" />
            </div>
            <TextInput
              id="phoneNumber"
              placeholder="The phone number of the facility"
              value={phoneNumber}
              onChange={(event) => setPhoneNumber(event.target.value)}
              required
            />
          </div>
          <div className="w-full">
            <Button onClick={handleSubmit}>
              {mode === "edit" ? "Save Changes" : "Create Facility"}
            </Button>
          </div>
        </div>
      </Modal.Body>
    </Modal>
  )
}
