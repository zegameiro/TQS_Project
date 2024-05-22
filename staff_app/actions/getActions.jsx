export const getAllFacilities = async (axios) => {
  return axios.get("facility/all").then((res) => {
    return res.data
  })
}

export const getAllRooms = async (axios) => {
  return axios.get("room/all").then((res) => {
    return res.data
  })
}

export const getRoomById = async (axios, id) => {
  return axios.get(`room/${id}`).then((res) => {
    return res.data
  })
}

export const getRoomsByFacilityID = async (axios, facilityID) => {
  return axios.get(`room/search?facilityID=${facilityID}`).then((res) => {
    return res.data
  })
}
