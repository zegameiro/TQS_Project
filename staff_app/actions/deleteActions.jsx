export const deleteFacility = (axios, id) => {
  return axios.delete(`facility/admin/delete?id=${id}`)
}

export const deleteRoom = (axios, id) => {
  return axios.delete(`room/admin/delete?id=${id}`)
}

export const deleteChair = (axios, id) => {
  return axios.delete(`chair/admin/delete?id=${id}`)
}

export const deleteEmployee = (axios, id) => {
  return axios.post(`employee/admin/delete/${id}`)
}
