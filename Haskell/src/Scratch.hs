{-# LANGUAGE TemplateHaskell #-}

module Scratch where
import Control.Lens

data User = User 
  { _id   :: Int
  , _name :: String
  , _logs :: [String]
  } deriving (Eq, Show)

data Post = Post
  { _p_title    :: String
  , _p_text     :: String
  , _p_author   :: User
  , _p_comments :: [Comment]
  , _p_likes    :: Int
  } deriving (Eq, Show)

data Comment = Comment
  { _c_content  :: String
  , _c_author   :: User
  , _c_comments :: [Comment]
  , _c_likes    :: Int
  } deriving (Eq, Show)

data Server = Server
  { content :: [Post]
  , users   :: [User]
  } deriving (Eq, Show)

$(makeLenses ''User)
$(makeLenses ''Post)
$(makeLenses ''Comment)
$(makeLenses ''Server)





