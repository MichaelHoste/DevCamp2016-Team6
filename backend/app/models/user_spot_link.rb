class UserSpotLink < ApplicationRecord

  # Associations

  belongs_to :user
  belongs_to :spot

  # CarrierWave

  mount_uploader :picture, PictureUploader

end
