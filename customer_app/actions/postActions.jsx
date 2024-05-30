// export const addNewFacility = async (axios, facilityData) => {
//   return axios.post("facility/admin/add", facilityData)
// }

// export const addNewRoom = async (axios, roomData) => {
//   return axios.post("room/admin/add", roomData)
// }

export const addNewReservation = async (axios, reservationData) => {
  return axios.post("reservation/add", reservationData)
}

export const checkInReservation = async (axios, reservationId) => { 
  return axios.post(`reservation/checkin/${reservationId}`)
}