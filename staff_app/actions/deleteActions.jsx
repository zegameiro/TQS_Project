export const deleteFacility = (axios, id) => {
  return axios.delete(`/facility/admin/delete?id=${id}`)
}