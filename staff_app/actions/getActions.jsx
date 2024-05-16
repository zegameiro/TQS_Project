export const getAllFacilities = async (axios) => {
  return axios.get("facility/all").then((res) => {
    return res.data;
  });
}