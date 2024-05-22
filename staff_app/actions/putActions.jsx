export const editFacility = async (axios, facilityData, id) => {
  return axios.put(`facility/admin/update?id=${id}`, facilityData)
}