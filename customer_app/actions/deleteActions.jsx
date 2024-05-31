// export const deleteFacility = (axios, id) => {
//   return axios.delete(`facility/admin/delete?id=${id}`)
// }

// export const deleteRoom = (axios, id) => {
//   return axios.delete(`room/admin/delete?id=${id}`)
// }

export const deleteReservation = (axios, id) => {
    return axios.delete(`reservation/${id}`)
}