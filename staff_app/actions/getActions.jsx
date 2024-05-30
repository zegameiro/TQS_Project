export const getAllFacilities = async (axios) => {
  return axios.get("facility/all").then((res) => {
    return res.data
  })
}

export const getFacilityById = async (axios, id) => {
  return axios.get(`facility/${id}`).then((res) => {
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

export const getChairById = async (axios, id) => {
  return axios.get(`chair/${id}`).then((res) => {
    return res.data
  })
}

export const getChairsByRoomID = async (axios, roomID) => {
  return axios.get(`chair/search?roomID=${roomID}`).then((res) => {
    return res.data
  })
}

export const getAllChairs = async (axios) => {
  return axios.get("chair/all").then((res) => {
    return res.data
  })
}

export const getAllReservations = async (axios) => {
  return axios.get("reservation/all").then((res) => {
    return res.data
  })
}
