export const addNewFacility = async (axios, facilityData) => {
  return axios.post("facility/admin/add", facilityData)
}

export const addNewRoom = async (axios, roomData) => {
  return axios.post("room/admin/add", roomData)
}

export const addNewChair = async (axios, chairData) => {
  return axios.post("chair/admin/add", chairData)
}