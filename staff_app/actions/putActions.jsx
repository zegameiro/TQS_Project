export const editFacility = async (axios, facilityData, id) => {
  return axios.put(`facility/admin/update?id=${id}`, facilityData)
}

export const editRoom = async (axios, roomData, id) => {
  return axios.put(`room/admin/update?id=${id}`, roomData)
}