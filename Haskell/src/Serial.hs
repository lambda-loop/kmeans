{-# LANGUAGE RecordWildCards #-}

module Serial2 where

import GHC.TypeNats

import Linear.Vector
-- TODO: sized
import qualified Data.Vector as V
import qualified Data.Vector.Mutable as MV
import Linear.V
import Control.Monad.ST

data RawData (d :: Natural) = RawData 
  { points    :: V.Vector (V d Double)
  , centroids :: V.Vector (V d Double) -- TODO: check mutable version
  } 

-- TODO: State version or even ST for performance when?
updateCentroids :: KnownNat d => RawData d  -> RawData d  
updateCentroids rawdata@RawData {..} = runST $ do
  let k = V.length centroids
  sums   <- MV.replicate k zero
  counts <- MV.replicate k 0

  V.forM_ points $ \point -> do
    let closestIdx = undefined -- V.minIndex @a @(k - 1) (distance point <$>)
    MV.modify sums   (^+^ point) closestIdx
    MV.modify counts (+ 1)       closestIdx

  cs <- V.generateM k $ \i -> do
    n <- MV.read counts i
    if n == 0
      then pure $ centroids V.! i
      else (^/ fromInteger n) <$> MV.read sums i

  pure $ rawdata {
    centroids = cs
  }

initializeRawData :: KnownNat d => Int -> V.Vector (V d Double) -> RawData d 
initializeRawData k points = runST $ do 
  sums   <- MV.replicate k zero
  counts <- MV.replicate k 0

  V.iforM_ points $ \i point -> do
    MV.modify sums (^+^ point) i
    MV.modify counts (+ 1) i

  cs <- V.generateM k $ \i -> 
    (^/) <$> MV.read sums i <*> MV.read counts i

  pure RawData { points    = points
               , centroids = cs 
               }


  
  
